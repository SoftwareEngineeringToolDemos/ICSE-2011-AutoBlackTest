/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

import mockit.internal.*;

@SuppressWarnings("UnusedDeclaration")
public final class FullVerificationsTest
{
   public static class Dependency
   {
      public void setSomething(int value) {}
      public void setSomethingElse(char value) {}
      public boolean editABunchMoreStuff() { return false; }
      public void notifyBeforeSave() {}
      public void prepare() {}
      public void save() {}
   }

   @Mocked Dependency mock;

   private void exerciseCodeUnderTest()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.setSomethingElse('a');
      mock.setSomething(45);
      mock.editABunchMoreStuff();
      mock.notifyBeforeSave();
      mock.save();
   }

   @Test
   public void verifyAllInvocations()
   {
      exerciseCodeUnderTest();

      new FullVerifications() {{
         mock.prepare(); minTimes = 1;
         mock.editABunchMoreStuff();
         mock.notifyBeforeSave(); maxTimes = 1;
         mock.setSomething(anyInt); minTimes = 0; maxTimes = 2;
         mock.setSomethingElse(anyChar);
         mock.save(); times = 1;
      }};
   }

   @Test
   public void verifyAllInvocationsWithSomeOfThemRecorded()
   {
      new NonStrictExpectations() {{
         mock.editABunchMoreStuff(); result = true;
         mock.setSomething(45);
      }};

      exerciseCodeUnderTest();

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(anyInt);
         mock.setSomethingElse(anyChar);
         mock.editABunchMoreStuff();
         mock.notifyBeforeSave();
         mock.save();
      }};
   }

   @Test
   public void verifyAllInvocationsWithThoseRecordedAsExpectedToOccurVerifiedImplicitly()
   {
      new NonStrictExpectations() {{
         mock.setSomething(45); times = 1;
         mock.editABunchMoreStuff(); result = true; minTimes = 1;
      }};

      exerciseCodeUnderTest();

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(123);
         mock.setSomethingElse(anyChar);
         mock.notifyBeforeSave();
         mock.save();
      }};
   }

   @Test
   public void verifyAllInvocationsExceptThoseAlreadyVerifiedInAPreviousVerificationBlock()
   {
      exerciseCodeUnderTest();

      new Verifications() {{
         mock.setSomething(45);
         mock.editABunchMoreStuff();
      }};

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(123);
         mock.setSomethingElse(anyChar);
         mock.notifyBeforeSave();
         mock.save();
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyAllInvocationsWithOneMissing()
   {
      exerciseCodeUnderTest();

      new FullVerifications() {{
         mock.prepare();
         mock.notifyBeforeSave();
         mock.setSomething(anyChar);
         mock.setSomethingElse(anyChar);
         mock.save();
      }};
   }

   @Test
   public void verifyUnrecordedInvocationThatWasExpectedToNotHappen()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.setSomething(45);

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(anyInt); times = 2;
         mock.notifyBeforeSave(); times = 0;
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyUnrecordedInvocationThatShouldNotHappenButDoes()
   {
      mock.setSomething(1);
      mock.notifyBeforeSave();

      new FullVerifications() {{
         mock.setSomething(1);
         mock.notifyBeforeSave(); times = 0;
      }};
   }

   @Test
   public void verifyInvocationThatIsAllowedToHappenAnyNumberOfTimesAndHappensOnce()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.save();

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(anyInt);
         mock.save(); minTimes = 0;
      }};
   }

   @Test
   public void verifyRecordedInvocationThatIsAllowedToHappenAnyNoOfTimesAndDoesNotHappen()
   {
      new NonStrictExpectations() {{ mock.save(); }};

      mock.prepare();
      mock.setSomething(123);

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(anyInt);
         mock.save(); minTimes = 0;
      }};
   }

   @Test
   public void verifyUnrecordedInvocationThatIsAllowedToHappenAnyNoOfTimesAndDoesNotHappen()
   {
      mock.prepare();
      mock.setSomething(123);

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(anyInt);
         mock.save(); minTimes = 0;
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyUnrecordedInvocationThatShouldHappenButDoesNot()
   {
      mock.setSomething(1);

      new FullVerifications() {{
         mock.notifyBeforeSave();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyRecordedInvocationThatShouldHappenButDoesNot()
   {
      new NonStrictExpectations() {{
         mock.notifyBeforeSave();
      }};

      mock.setSomething(1);

      new FullVerifications() {{
         mock.notifyBeforeSave();
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyAllInvocationsWithExpectationRecordedButOneInvocationUnverified()
   {
      new NonStrictExpectations() {{
         mock.setSomething(anyInt);
      }};

      mock.setSomething(123);
      mock.editABunchMoreStuff();
      mock.setSomething(45);

      new FullVerifications() {{
         mock.setSomething(withNotEqual(123));
         mock.editABunchMoreStuff();
      }};
   }

   @Ignore @Test
   public void verifyTwoInvocationsWithIteratingBlockHavingExpectationRecordedAndSecondInvocationUnverified()
   {
      new NonStrictExpectations() {{
         mock.setSomething(anyInt);
      }};

      mock.setSomething(123);
      mock.setSomething(45);

      try {
         new FullVerifications(2) {{ mock.setSomething(123); }};
         fail();
      }
      catch (AssertionError e) {
         assertTrue(e.getMessage().contains("Missing 1 invocation"));
      }
   }

   @Test(expected = MissingInvocation.class)
   public void verifyAllInvocationsWithExtraVerification()
   {
      mock.prepare();
      mock.setSomething(123);

      new FullVerifications() {{
         mock.prepare();
         mock.setSomething(123);
         mock.notifyBeforeSave();
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyAllInvocationsWithInvocationCountOneLessThanActual()
   {
      mock.setSomething(123);
      mock.setSomething(45);

      new FullVerifications() {{
         mock.setSomething(anyInt); times = 1;
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyAllInvocationsWithInvocationCountTwoLessThanActual()
   {
      mock.setSomething(123);
      mock.setSomething(45);
      mock.setSomething(0);

      new FullVerifications() {{
         mock.setSomething(anyInt); times = 1;
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyAllInvocationsWithInvocationCountMoreThanActual()
   {
      mock.setSomethingElse('f');

      new FullVerifications() {{
         mock.setSomethingElse(anyChar);
         times = 3;
      }};
   }

   @Test
   public void verifyAllInvocationsInIteratingBlock()
   {
      mock.setSomething(123);
      mock.save();
      mock.setSomething(45);
      mock.save();

      new FullVerifications(2) {{
         mock.setSomething(anyInt);
         mock.save();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifySingleInvocationInBlockWithLargerNumberOfIterations()
   {
      mock.setSomething(123);

      new FullVerifications(3) {{
         mock.setSomething(123);
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyMultipleInvocationsInBlockWithSmallerNumberOfIterations()
   {
      mock.setSomething(123);
      mock.setSomething(-14);

      new FullVerifications(1) {{
         mock.setSomething(anyInt);
      }};
   }

   @Test
   public void verifyWithArgumentMatcherAndIndividualInvocationCountsInIteratingBlock()
   {
      for (int i = 0; i < 2; i++) {
         exerciseCodeUnderTest();
      }

      new FullVerifications(2) {{
         mock.prepare(); maxTimes = 1;
         mock.setSomething(anyInt); minTimes = 2;
         mock.setSomethingElse('a');
         mock.editABunchMoreStuff(); minTimes = 0; maxTimes = 5;
         mock.notifyBeforeSave();
         mock.save(); times = 1;
      }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyNoInvocationsOccurredOnMockedDependencyWithOneHavingOccurred()
   {
      mock.editABunchMoreStuff();

      new FullVerifications() {};
   }

   @Test
   public void verifyNoInvocationsOnMockedDependencyBeyondThoseRecordedAsExpected()
   {
      new NonStrictExpectations() {{
         mock.prepare(); times = 1;
      }};

      new NonStrictExpectations() {{
         mock.setSomething(anyInt); minTimes = 1;
         mock.save(); times = 1;
      }};

      mock.prepare();
      mock.setSomething(1);
      mock.setSomething(2);
      mock.save();

      new FullVerifications() {};
   }

   @Test
   public void verifyNoInvocationsOnMockedDependencyBeyondThoseRecordedAsExpectedWithOneHavingOccurred()
   {
      new NonStrictExpectations() {{
         mock.prepare(); times = 1;
         mock.save(); minTimes = 1;
      }};

      mock.prepare();
      mock.editABunchMoreStuff();
      mock.save();

      try {
         new FullVerifications() {};
         fail();
      }
      catch (UnexpectedInvocation e) {
         assertTrue(e.getMessage().contains("editABunchMoreStuff()"));
      }
   }
}
