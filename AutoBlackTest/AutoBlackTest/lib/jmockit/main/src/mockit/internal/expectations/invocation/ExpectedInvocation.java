/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.invocation;

import java.util.*;

import mockit.internal.*;
import mockit.external.asm4.Type;

import mockit.internal.expectations.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class ExpectedInvocation
{
   private static final Object UNDEFINED_DEFAULT_RETURN = new Object();

   public final Object instance;
   public boolean matchInstance;
   public final InvocationArguments arguments;
   public CharSequence customErrorMessage;
   private final ExpectationError invocationCause;
   private Object defaultReturnValue;
   private Object cascadedMock;

   public ExpectedInvocation(
      Object mock, int access, String mockedClassDesc, String mockNameAndDesc, boolean matchInstance, Object[] args)
   {
      this(mock, access, mockedClassDesc, mockNameAndDesc, matchInstance, null, null, args);
   }

   public ExpectedInvocation(
      Object mock, int access, String mockedClassDesc, String mockNameAndDesc, boolean matchInstance,
      String genericSignature, String exceptions, Object[] args)
   {
      instance = mock;
      this.matchInstance = matchInstance;
      arguments = new InvocationArguments(access, mockedClassDesc, mockNameAndDesc, genericSignature, exceptions, args);
      invocationCause = new ExpectationError();
      determineDefaultReturnValueFromMethodSignature();
   }

   private void determineDefaultReturnValueFromMethodSignature()
   {
      Object rv = Utilities.evaluateObjectOverride(instance, getMethodNameAndDescription(), getArgumentValues());
      defaultReturnValue = rv == null ? UNDEFINED_DEFAULT_RETURN : rv;
   }

   // Simple getters //////////////////////////////////////////////////////////////////////////////////////////////////

   public String getClassDesc() { return arguments.classDesc; }
   public String getClassName() { return arguments.getClassName(); }
   public String getMethodNameAndDescription() { return arguments.methodNameAndDesc; }
   public Object[] getArgumentValues() { return arguments.getValues(); }

   // Matching based on instance or mocked type ///////////////////////////////////////////////////////////////////////

   public boolean isMatch(String invokedClassDesc, String invokedMethod)
   {
      return invokedClassDesc.equals(getClassDesc()) && isMatchingMethod(invokedMethod);
   }

   public boolean isMatch(
      Object replayInstance, String invokedClassDesc, String invokedMethod, Map<Object, Object> instanceMap)
   {
      return
         isMatch(invokedClassDesc, invokedMethod) &&
         (getMethodNameAndDescription().charAt(0) == '<' ||
          !matchInstance || isEquivalentInstance(replayInstance, instanceMap));
   }

   private boolean isMatchingMethod(String invokedMethod)
   {
      String nameAndDesc = getMethodNameAndDescription();
      int i = 0;

      // Will return false if the method names or parameters are different:
      while (true) {
         char c = nameAndDesc.charAt(i);

         if (c != invokedMethod.charAt(i)) {
            return false;
         }

         i++;

         if (c == ')') {
            break;
         }
      }

      int n = invokedMethod.length();

      if (n == nameAndDesc.length()) {
         int j = i;

         // Given return types of same length, will return true if they are identical:
         while (true) {
            char c = nameAndDesc.charAt(j);

            if (c != invokedMethod.charAt(j)) {
               break;
            }

            j++;

            if (j == n) {
               return true;
            }
         }
      }

      // At this point the methods are known to differ only in return type, so check if the return
      // type of the recorded one is assignable to the return type of the one invoked:
      Type rt1 = Type.getType(nameAndDesc.substring(i));
      Type rt2 = Type.getType(invokedMethod.substring(i));

      return Utilities.getClassForType(rt2).isAssignableFrom(Utilities.getClassForType(rt1));
   }

   public boolean isEquivalentInstance(Object mockedInstance, Map<Object, Object> instanceMap)
   {
      return
         mockedInstance == instance || mockedInstance == instanceMap.get(instance) ||
         TestRun.getExecutingTest().isInvokedInstanceEquivalentToCapturedInstance(instance, mockedInstance);
   }

   // Creation of Error instances for invocation mismatch reporting ///////////////////////////////////////////////////

   public ExpectedInvocation(Object mockedInstance, String classDesc, String methodNameAndDesc, Object[] args)
   {
      instance = mockedInstance;
      matchInstance = false;
      arguments = new InvocationArguments(0, classDesc, methodNameAndDesc, null, null, args);
      invocationCause = null;
   }

   public UnexpectedInvocation errorForUnexpectedInvocation()
   {
      return newUnexpectedInvocationWithCause("Unexpected invocation", "Unexpected invocation of" + this);
   }

   private UnexpectedInvocation newUnexpectedInvocationWithCause(String titleForCause, String initialMessage)
   {
      String errorMessage = getErrorMessage(initialMessage);
      UnexpectedInvocation error = new UnexpectedInvocation(errorMessage);
      setErrorAsInvocationCause(titleForCause, error);
      return error;
   }

   private String getErrorMessage(String initialMessage)
   {
      return customErrorMessage == null ? initialMessage : customErrorMessage + "\n" + initialMessage;
   }

   private void setErrorAsInvocationCause(String titleForCause, Error error)
   {
      if (invocationCause != null) {
         invocationCause.defineCause(titleForCause, error);
      }
   }

   private MissingInvocation newMissingInvocationWithCause(String titleForCause, String initialMessage)
   {
      String errorMessage = getErrorMessage(initialMessage);
      MissingInvocation error = new MissingInvocation(errorMessage);
      setErrorAsInvocationCause(titleForCause, error);
      return error;
   }

   public MissingInvocation errorForMissingInvocation()
   {
      return newMissingInvocationWithCause("Missing invocation", "Missing invocation of" + this);
   }

   public MissingInvocation errorForMissingInvocations(int totalMissing)
   {
      String message = "Missing " + totalMissing + invocationsToThis(totalMissing);
      return newMissingInvocationWithCause("Missing invocations", message);
   }

   private String invocationsToThis(int invocations)
   {
      String prefix = invocations == 1 ? " invocation to" : " invocations to";
      return prefix + this;
   }

   public UnexpectedInvocation errorForUnexpectedInvocation(
      Object mock, String invokedClassDesc, String invokedMethod)
   {
      String instanceDescription = mock == null ? "" : "\non instance: " + Utilities.objectIdentity(mock);
      String message =
         "Unexpected invocation of:\n" + new MethodFormatter(invokedClassDesc, invokedMethod) + instanceDescription +
         "\nwhen was expecting an invocation of" + this;
      return newUnexpectedInvocationWithCause("Unexpected invocation", message);
   }

   public UnexpectedInvocation errorForUnexpectedInvocations(int totalUnexpected)
   {
      String message = totalUnexpected + " unexpected" + invocationsToThis(totalUnexpected);
      return newUnexpectedInvocationWithCause("Unexpected invocations", message);
   }

   public UnexpectedInvocation errorForUnexpectedInvocationBeforeAnother(ExpectedInvocation another)
   {
      return newUnexpectedInvocationWithCause("Unexpected invocation" + this, "Unexpected invocation before" + another);
   }

   public UnexpectedInvocation errorForUnexpectedInvocationAfterAnother(ExpectedInvocation another)
   {
      return newUnexpectedInvocationWithCause("Unexpected invocation" + this, "Unexpected invocation after" + another);
   }

   @Override
   public String toString()
   {
      String desc = arguments.toString();

      if (instance != null) {
         desc += "\non mock instance: " + Utilities.objectIdentity(instance);
      }

      return desc;
   }

   String toString(Object[] actualInvocationArguments)
   {
      Object[] invocationArgs = arguments.getValues();
      arguments.setValues(actualInvocationArguments);
      String description = toString();
      arguments.setValues(invocationArgs);
      return description;
   }

   // Default result //////////////////////////////////////////////////////////////////////////////////////////////////

   public Object getDefaultResult()
   {
      return TestRun.getExecutingTest().defaultResults.get(arguments.getGenericSignature(), arguments.exceptions);
   }

   public Object getDefaultValueForReturnType(TestOnlyPhase phase)
   {
      if (defaultReturnValue == UNDEFINED_DEFAULT_RETURN) {
         Object defaultResult = getDefaultResult();

         if (defaultResult != null) {
            return defaultResult;
         }

         String returnTypeDesc = DefaultValues.getReturnTypeDesc(arguments.methodNameAndDesc);
         defaultReturnValue = DefaultValues.computeForType(returnTypeDesc);

         if (defaultReturnValue == null) {
            produceCascadedInstanceIfApplicable(phase, returnTypeDesc);
         }
      }

      return defaultReturnValue;
   }

   private void produceCascadedInstanceIfApplicable(TestOnlyPhase phase, String returnTypeDesc)
   {
      String mockedTypeDesc = getClassDesc();
      cascadedMock = MockedTypeCascade.getMock(mockedTypeDesc, instance, returnTypeDesc);

      if (cascadedMock != null) {
         if (phase != null) {
            phase.setNextInstanceToMatch(cascadedMock);
         }

         defaultReturnValue = cascadedMock;
      }
   }

   public Object getCascadedMock() { return cascadedMock; }

   public void copyDefaultReturnValue(ExpectedInvocation other) { defaultReturnValue = other.defaultReturnValue; }
}
