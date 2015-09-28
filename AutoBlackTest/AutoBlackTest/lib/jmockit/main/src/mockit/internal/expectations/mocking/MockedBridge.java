/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.expectations.mocking;

import java.lang.reflect.*;

import mockit.internal.*;
import mockit.internal.expectations.*;
import mockit.internal.state.*;
import mockit.internal.util.*;

public final class MockedBridge extends MockingBridge
{
   @SuppressWarnings("UnusedDeclaration")
   public static final InvocationHandler MB = new MockedBridge();

   public static void preventEventualClassLoadingConflicts()
   {
      // Pre-load certain JMockit classes to avoid NoClassDefFoundError's when mocking certain JRE classes,
      // such as ArrayList.
      try {
         Class.forName("mockit.Capturing");
         Class.forName("mockit.Delegate");
         Class.forName("mockit.internal.expectations.invocation.InvocationResults");
         Class.forName("mockit.internal.expectations.mocking.BaseTypeRedefinition$MockedClass");
         Class.forName("mockit.internal.expectations.mocking.SharedFieldTypeRedefinitions");
         Class.forName("mockit.internal.expectations.mocking.TestedClasses");
         Class.forName("mockit.internal.expectations.argumentMatching.EqualityMatcher");
      }
      catch (ClassNotFoundException ignore) {}

      wasCalledDuringClassLoading();
      DefaultValues.computeForReturnType("()J");
   }

   public Object invoke(Object mocked, Method method, Object[] args) throws Throwable
   {
      String mockedClassDesc = (String) args[1];

      if (notToBeMocked(mocked, mockedClassDesc)) {
         return Void.class;
      }

      String mockName = (String) args[2];
      String mockDesc = (String) args[3];
      Object[] mockArgs = extractMockArguments(args);
      String mockNameAndDesc = mockName.concat(mockDesc); // avoid possible use of mocked StringBuilder
      int executionMode = (Integer) args[6];

      if (executionMode == 0 && !"toString".equals(mockName) && RecordAndReplayExecution.LOCK.isHeldByCurrentThread()) {
         return RecordAndReplayExecution.defaultReturnValue(mocked, mockNameAndDesc, 1, mockArgs);
      }

      if (TestRun.isInsideNoMockingZone()) {
         return Void.class;
      }

      TestRun.enterNoMockingZone();

      try {
         int mockAccess = (Integer) args[0];
         String genericSignature = (String) args[4];
         String exceptions = (String) args[5];

         return
            RecordAndReplayExecution.recordOrReplay(
               mocked, mockAccess, mockedClassDesc, mockNameAndDesc, genericSignature, exceptions,
               executionMode, mockArgs);
      }
      finally {
         TestRun.exitNoMockingZone();
      }
   }
}
