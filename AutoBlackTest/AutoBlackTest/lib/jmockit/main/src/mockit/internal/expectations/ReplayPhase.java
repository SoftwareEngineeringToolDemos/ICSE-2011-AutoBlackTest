/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

import mockit.internal.expectations.invocation.*;
import mockit.internal.state.*;

final class ReplayPhase extends Phase
{
   private int initialStrictExpectationIndexForCurrentBlock;
   int currentStrictExpectationIndex;
   final List<Expectation> nonStrictInvocations;
   final List<Object[]> nonStrictInvocationArguments;
   private Expectation nonStrictExpectation;

   ReplayPhase(RecordAndReplayExecution recordAndReplay)
   {
      super(recordAndReplay);
      nonStrictInvocations = new ArrayList<Expectation>();
      nonStrictInvocationArguments = new ArrayList<Object[]>();
      initialStrictExpectationIndexForCurrentBlock =
         Math.max(recordAndReplay.lastExpectationIndexInPreviousReplayPhase, 0);
      positionOnFirstStrictInvocation();
   }

   private void positionOnFirstStrictInvocation()
   {
      List<Expectation> expectations = getExpectations();

      if (expectations.isEmpty()) {
         currentStrictExpectationIndex = -1;
         currentExpectation = null ;
      }
      else {
         currentStrictExpectationIndex = initialStrictExpectationIndexForCurrentBlock;
         currentExpectation =
            currentStrictExpectationIndex < expectations.size() ?
               expectations.get(currentStrictExpectationIndex) : null;
      }
   }

   private List<Expectation> getExpectations() { return recordAndReplay.executionState.expectations; }

   @Override
   Object handleInvocation(
      Object mock, int mockAccess, String mockClsDesc, String mockDesc, String genericSignature, String exceptions,
      boolean withRealImpl, Object[] args) throws Throwable
   {
      nonStrictExpectation = recordAndReplay.executionState.findNonStrictExpectation(mock, mockClsDesc, mockDesc, args);

      if (nonStrictExpectation == null) {
         createExpectationIfNonStrictInvocation(
            mock, mockAccess, mockClsDesc, mockDesc, genericSignature, exceptions, args);
      }

      if (nonStrictExpectation != null) {
         nonStrictInvocations.add(nonStrictExpectation);
         nonStrictInvocationArguments.add(args);
         return updateConstraintsAndProduceResult(mock, withRealImpl, args);
      }

      return handleStrictInvocation(mock, mockClsDesc, mockDesc, withRealImpl, args);
   }

   private void createExpectationIfNonStrictInvocation(
      Object mock, int mockAccess, String mockClassDesc, String mockNameAndDesc, String genericSignature,
      String exceptions, Object[] args)
   {
      if (!TestRun.getExecutingTest().isStrictInvocation(mock, mockClassDesc, mockNameAndDesc)) {
         ExpectedInvocation invocation =
            new ExpectedInvocation(
               mock, mockAccess, mockClassDesc, mockNameAndDesc, false, genericSignature, exceptions, args);
         nonStrictExpectation = new Expectation(null, invocation, true);
         recordAndReplay.executionState.addExpectation(nonStrictExpectation, true);
      }
   }

   private Object updateConstraintsAndProduceResult(Object mock, boolean withRealImpl, Object[] args) throws Throwable
   {
      boolean executeRealImpl = withRealImpl && nonStrictExpectation.recordPhase == null;
      nonStrictExpectation.constraints.incrementInvocationCount();

      if (executeRealImpl) {
         nonStrictExpectation.executedRealImplementation = true;
         Object defaultResult = nonStrictExpectation.invocation.getDefaultResult();
         return defaultResult == null ? Void.class : defaultResult;
      }

      if (nonStrictExpectation.constraints.isInvocationCountMoreThanMaximumExpected()) {
         recordAndReplay.setErrorThrown(nonStrictExpectation.invocation.errorForUnexpectedInvocations(1));
         return null;
      }

      return nonStrictExpectation.produceResult(mock, args);
   }

   @SuppressWarnings("OverlyComplexMethod")
   private Object handleStrictInvocation(
      Object mock, String mockClassDesc, String mockNameAndDesc, boolean withRealImpl, Object[] replayArgs)
      throws Throwable
   {
      Map<Object, Object> instanceMap = getInstanceMap();

      while (true) {
         if (currentExpectation == null) {
            return handleUnexpectedInvocation(mock, mockClassDesc, mockNameAndDesc, withRealImpl, replayArgs);
         }

         ExpectedInvocation invocation = currentExpectation.invocation;

         if (invocation.isMatch(mock, mockClassDesc, mockNameAndDesc, instanceMap)) {
            if (mock != invocation.instance) {
               instanceMap.put(invocation.instance, mock);
            }

            Error error = invocation.arguments.assertMatch(replayArgs, instanceMap);

            if (error != null) {
               if (currentExpectation.constraints.isInvocationCountInExpectedRange()) {
                  moveToNextExpectation();
                  continue;
               }

               if (withRealImpl) {
                  return Void.class;
               }

               recordAndReplay.setErrorThrown(error);
               return null;
            }

            Expectation expectation = currentExpectation;

            if (expectation.constraints.incrementInvocationCount()) {
               moveToNextExpectation();
            }
            else if (expectation.constraints.isInvocationCountMoreThanMaximumExpected()) {
               recordAndReplay.setErrorThrown(invocation.errorForUnexpectedInvocations(1));
               return null;
            }

            return expectation.produceResult(mock, replayArgs);
         }
         else if (currentExpectation.constraints.isInvocationCountInExpectedRange()) {
            moveToNextExpectation();
         }
         else if (withRealImpl) {
            return Void.class;
         }
         else {
            recordAndReplay.setErrorThrown(
               invocation.errorForUnexpectedInvocation(mock, mockClassDesc, mockNameAndDesc));
            return null;
         }
      }
   }

   private Object handleUnexpectedInvocation(
      Object mock, String mockClassDesc, String mockNameAndDesc, boolean withRealImpl, Object[] replayArgs)
   {
      if (withRealImpl) {
         return Void.class;
      }

      recordAndReplay.setErrorThrown(
         new ExpectedInvocation(mock, mockClassDesc, mockNameAndDesc, replayArgs).errorForUnexpectedInvocation());

      return null;
   }

   private void moveToNextExpectation()
   {
      List<Expectation> expectations = getExpectations();
      RecordPhase expectationBlock = currentExpectation.recordPhase;
      currentStrictExpectationIndex++;

      currentExpectation =
         currentStrictExpectationIndex < expectations.size() ? expectations.get(currentStrictExpectationIndex) : null;

      if (expectationBlock.numberOfIterations <= 1) {
         if (currentExpectation != null && currentExpectation.recordPhase != expectationBlock) {
            initialStrictExpectationIndexForCurrentBlock = currentStrictExpectationIndex;
         }
      }
      else if (currentExpectation == null || currentExpectation.recordPhase != expectationBlock) {
         expectationBlock.numberOfIterations--;
         positionOnFirstStrictInvocation();
         resetInvocationCountsForStrictExpectations(expectationBlock);
      }
   }

   private void resetInvocationCountsForStrictExpectations(RecordPhase expectationBlock)
   {
      for (Expectation expectation : getExpectations()) {
         if (expectation.recordPhase == expectationBlock) {
            expectation.constraints.invocationCount = 0;
         }
      }
   }

   Error endExecution()
   {
      Expectation strict = currentExpectation;
      currentExpectation = null;

      if (strict != null && strict.constraints.isInvocationCountLessThanMinimumExpected()) {
         return strict.invocation.errorForMissingInvocation();
      }

      for (Expectation nonStrict : recordAndReplay.executionState.nonStrictExpectations) {
         InvocationConstraints constraints = nonStrict.constraints;

         if (constraints.isInvocationCountLessThanMinimumExpected()) {
            return constraints.errorForMissingExpectations(nonStrict.invocation);
         }
      }

      int nextStrictExpectationIndex = currentStrictExpectationIndex + 1;

      if (nextStrictExpectationIndex < getExpectations().size()) {
         Expectation nextStrictExpectation = getExpectations().get(nextStrictExpectationIndex);

         if (nextStrictExpectation.constraints.isInvocationCountLessThanMinimumExpected()) {
            return nextStrictExpectation.invocation.errorForMissingInvocation();
         }
      }

      return null;
   }
}
