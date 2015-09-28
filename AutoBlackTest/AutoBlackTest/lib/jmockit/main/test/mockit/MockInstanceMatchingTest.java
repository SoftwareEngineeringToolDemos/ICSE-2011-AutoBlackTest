/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.concurrent.*;
import javax.sql.*;

import static org.junit.Assert.*;
import org.junit.*;

import mockit.internal.*;

public final class MockInstanceMatchingTest
{
   static class Collaborator
   {
      private int value;

      int getValue() { return value; }
      void setValue(int value) { this.value = value; }
   }

   @Mocked Collaborator mock;
   @Mocked DataSource mockDS1;
   @Mocked DataSource mockDS2;

   @Test
   public void recordExpectationMatchingOnMockInstance()
   {
      new Expectations() {{
         onInstance(mock).getValue();
         result = 12;
      }};

      assertEquals(12, mock.getValue());
   }

   @Test(expected = MissingInvocation.class)
   public void recordOnMockInstanceButReplayOnDifferentInstance()
   {
      Collaborator collaborator = new Collaborator();

      new Expectations() {{
         onInstance(mock).getValue();
         result = 12;
      }};

      assertEquals(0, collaborator.getValue());
   }

   @Test
   public void verifyExpectationMatchingOnMockInstance()
   {
      new Collaborator().setValue(12);
      mock.setValue(12);

      new Verifications() {{
         mock.setValue(anyInt); times = 2;
         onInstance(mock).setValue(12); times = 1;
      }};
   }

   @Test
   public void verifyExpectationsOnSameMethodCallForDifferentMockedInstances()
   {
      final Collaborator c1 = new Collaborator();
      c1.getValue();
      mock.getValue();
      final Collaborator c2 = new Collaborator();
      c2.getValue();

      new Verifications() {{
         onInstance(mock).getValue(); times = 1;
         onInstance(c1).getValue(); times = 1;
         onInstance(c2).getValue(); times = 1;
         mock.getValue(); times = 3;
         c1.getValue(); times = 3;
         c2.getValue(); times = 3;
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyOnMockInstanceButReplayOnDifferentInstance()
   {
      new Collaborator().setValue(12);

      new Verifications() {{
         onInstance(mock).setValue(12);
      }};
   }

   @Test
   public void recordExpectationsMatchingOnMultipleMockInstances(final Collaborator mock2)
   {
      new Expectations() {{
         mock.getValue(); result = 12;
         mock2.getValue(); result = 13;
         mock.setValue(20);
      }};

      assertEquals(12, mock.getValue());
      assertEquals(13, mock2.getValue());
      mock.setValue(20);
   }

   @Test(expected = UnexpectedInvocation.class)
   public void recordOnSpecificMockInstancesButReplayOnDifferentOnes(final Collaborator mock2)
   {
      new Expectations() {{
         mock.setValue(12);
         mock2.setValue(13);
      }};

      mock2.setValue(12);
      mock.setValue(13);
   }

   @Test
   public void verifyExpectationsMatchingOnMultipleMockInstances(final Collaborator mock2)
   {
      mock.setValue(12);
      mock2.setValue(13);
      mock.setValue(20);

      new VerificationsInOrder() {{
         mock.setValue(12);
         mock2.setValue(13);
         mock.setValue(20);
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyOnSpecificMockInstancesButReplayOnDifferentOnes(final Collaborator mock2)
   {
      mock2.setValue(12);
      mock.setValue(13);

      new FullVerifications() {{
         mock.setValue(12);
         mock2.setValue(13);
      }};
   }

   @Test(expected = NullPointerException.class)
   public void recordOnNullMockInstance()
   {
      new Expectations() {{
         Collaborator mock2 = null;
         onInstance(mock2).getValue();
      }};
   }

   @Test(expected = NullPointerException.class)
   public void verifyOnNullMockInstance()
   {
      new Verifications() {{
         Collaborator mock2 = null;
         onInstance(mock2).getValue();
      }};
   }

   @Test
   public void matchOnTwoMockInstancesWithNonStrictExpectations(final Collaborator mock2)
   {
      new NonStrictExpectations() {{
         mock.getValue(); result = 1; times = 1;
         mock2.getValue(); result = 2; times = 1;
      }};

      assertEquals(1, mock.getValue());
      assertEquals(2, mock2.getValue());
   }

   @Test
   public void matchOnTwoMockInstancesWithNonStrictExpectationsAndReplayInDifferentOrder(final Collaborator mock2)
   {
      new NonStrictExpectations() {{
         mock.getValue(); result = 1;
         mock2.getValue(); result = 2;
      }};

      assertEquals(2, mock2.getValue());
      assertEquals(1, mock.getValue());
      assertEquals(1, mock.getValue());
      assertEquals(2, mock2.getValue());
   }

   @Test
   public void matchOnTwoMockInstancesForOtherwiseIdenticalExpectations(final Collaborator mock2)
   {
      mock.getValue();
      mock2.getValue();
      mock2.setValue(1);
      mock.setValue(1);

      new Verifications() {{
         mock.getValue(); times = 1;
         mock2.getValue(); times = 1;
      }};

      new VerificationsInOrder() {{
         mock2.setValue(1);
         mock.setValue(1);
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void recordExpectationsMatchingOnMultipleMockParametersButReplayOutOfOrder(
      final Runnable r1, final Runnable r2)
   {
      new Expectations() {{
         r2.run();
         r1.run();
      }};

      r1.run();
      r2.run();
   }

   @Test(expected = MissingInvocation.class)
   public void verifyExpectationsMatchingOnMultipleMockParametersButReplayedOutOfOrder(
      final AbstractExecutorService es1, final AbstractExecutorService es2)
   {
      es2.execute(null);
      es1.submit((Runnable) null);

      new FullVerificationsInOrder() {{
         es1.execute((Runnable) any);
         es2.submit((Runnable) any);
      }};
   }

   @Test
   public void recordExpectationMatchingOnInstanceCreatedInsideCodeUnderTest()
   {
      new Expectations() {{
         onInstance(new Collaborator()).getValue(); result = 1;
      }};

      assertEquals(1, new Collaborator().getValue());
   }

   @Test
   public void declaringNonStrictMockedTypeImpliesNonStrictnessForOtherMocksOfSameTypeAsWell(
      final Collaborator mock1, @NonStrict Collaborator mock2)
   {
      // "mock1" is regarded as non-strict because "mock2" specifies non-strictness for *all* instances:
      new Expectations() {{ mock1.setValue(5); }};

      assertEquals(0, mock2.getValue());
      // Call to mock1.setValue is not missed.
   }

   @Test
   public void declaringNonStrictMockedTypeImpliesNonStrictnessForOtherMocksOfSameTypeAsWell2(final Collaborator mock1)
   {
      new Expectations() {
         @NonStrict Collaborator mock2;

         {
            // "mock1" is regarded as non-strict because "mock2" specifies non-strictness for *all* instances:
            mock1.setValue(5);
         }
      };

      // Call to mock1.getValue is not considered to be unexpected, since "Collaborator" is non-strict.
      mock1.getValue();
   }

   @Test
   public void recordAllowedConstructorInvocationForMockedTypeWithBothStrictAndNonStrictMocks(
      @NonStrict Collaborator mock1, final Collaborator mock2)
   {
      new Expectations() {{
         mock2.setValue(2);
         new Collaborator();
      }};

      mock2.setValue(2);
   }

   @Test
   public void unexpectedConstructorInvocationForMockedTypeWithBothStrictAndNonStrictMocks(
      @NonStrict Collaborator mock1, final Collaborator mock2)
   {
      new Expectations() {{
         mock2.getValue(); result = 2;
      }};

      new Collaborator().setValue(1);
      assertEquals(2, mock2.getValue());
   }

   @Test
   public void recordExpectationsOnTwoInstancesOfSameMockedInterface() throws Exception
   {
      new NonStrictExpectations() {{
         mockDS1.getLoginTimeout(); result = 1000;
         mockDS2.getLoginTimeout(); result = 2000;
      }};

      assertNotSame(mockDS1, mockDS2);
      assertEquals(1000, mockDS1.getLoginTimeout());
      assertEquals(2000, mockDS2.getLoginTimeout());
      mockDS2.setLoginTimeout(3000);

      new Verifications() {{ mockDS2.setLoginTimeout(anyInt); }};
   }

   static class BaseClass { final void doSomething() {} }
   static final class SubclassA extends BaseClass { void doSomethingElse() {} }
   static final class SubclassB extends BaseClass { void doSomethingElse() {} }

   @Test
   public void verifyingCallsOnSpecificInstancesOfDifferentSubclasses(final SubclassA a, final SubclassB b)
   {
      a.doSomething();
      new BaseClass().doSomething();
      b.doSomething();
      a.doSomethingElse();
      new SubclassA().doSomethingElse();
      b.doSomethingElse();

      new Verifications() {{
         a.doSomethingElse(); times = 2;
         b.doSomethingElse(); times = 1;
         onInstance(a).doSomething(); times = 1;
         onInstance(b).doSomething(); times = 1;
      }};
   }
}
