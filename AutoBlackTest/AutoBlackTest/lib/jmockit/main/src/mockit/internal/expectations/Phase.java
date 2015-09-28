/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations;

import java.util.*;

abstract class Phase
{
   final RecordAndReplayExecution recordAndReplay;
   Expectation currentExpectation;

   Phase(RecordAndReplayExecution recordAndReplay) { this.recordAndReplay = recordAndReplay; }

   final Expectation getCurrentExpectation()
   {
      validatePresenceOfExpectation(currentExpectation);
      return currentExpectation;
   }

   final void validatePresenceOfExpectation(Expectation expectation)
   {
      if (expectation == null) {
         throw new IllegalStateException(
            "Missing invocation to mocked type at this point; please make sure such invocations appear only after " +
            "the declaration of a suitable mock field or parameter");
      }
   }

   final Map<Object, Object> getInstanceMap() { return recordAndReplay.executionState.instanceMap; }

   abstract Object handleInvocation(
      Object mock, int mockAccess, String mockClassDesc, String mockNameAndDesc, String genericSignature,
      String exceptions, boolean withRealImpl, Object[] args) throws Throwable;
}
