/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

import mockit.internal.*;

public final class VerificationsTest
{
   @SuppressWarnings("UnusedParameters")
   public static class Dependency
   {
      public Dependency() {}
      private Dependency(int i) {}

      public void setSomething(int value) {}
      public void setSomethingElse(String value) {}
      public void editABunchMoreStuff() {}
      public void notifyBeforeSave() {}
      public void prepare() {}
      public void save() {}

      private void privateMethod() {}
      private static void privateStaticMethod(String s, boolean b) {}
   }

   @Mocked Dependency mock;

   private void exerciseCodeUnderTest()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.setSomethingElse("anotherValue");
      mock.setSomething(45);
      mock.editABunchMoreStuff();
      mock.notifyBeforeSave();
      mock.save();
   }

   @Test
   public void verifySimpleInvocations()
   {
      exerciseCodeUnderTest();

      new Verifications() {{
         mock.prepare(); times = 1;
         mock.editABunchMoreStuff();
         mock.setSomething(45);
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyUnrecordedInvocationThatNeverHappens()
   {
      mock.setSomething(123);
      mock.prepare();

      new Verifications() {{
         mock.editABunchMoreStuff();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyRecordedInvocationThatNeverHappens()
   {
      new NonStrictExpectations() {{
         mock.editABunchMoreStuff();
      }};

      mock.setSomething(123);
      mock.prepare();

      new Verifications() {{
         mock.editABunchMoreStuff();
      }};
   }

   @Test
   public void verifyInvocationThatIsAllowedToHappenAnyNumberOfTimesAndHappensOnce()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.save();

      new Verifications() {{
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

      new Verifications() {{
         mock.prepare();
         mock.save(); minTimes = 0;
      }};
   }

   @Test
   public void verifyRecordedInvocationWithExactInvocationCountUsingArgumentMatchers()
   {
      new NonStrictExpectations() {{ mock.setSomething(anyInt); }};

      mock.setSomething(1);
      mock.setSomething(2);

      new Verifications() {{
         mock.setSomething(anyInt);
         times = 2;
      }};
   }

   @Test
   public void verifyUnrecordedInvocationThatIsAllowedToHappenAnyNoOfTimesAndDoesNotHappen()
   {
      mock.prepare();
      mock.setSomething(123);

      new Verifications() {{
         mock.prepare();
         mock.setSomething(anyInt);
         mock.save(); minTimes = 0;
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyUnrecordedInvocationThatShouldHappenButDoesNot()
   {
      mock.setSomething(1);

      new Verifications() {{ mock.notifyBeforeSave(); }};
   }

   @Test
   public void verifyInvocationsWithInvocationCount()
   {
      mock.setSomething(3);
      mock.save();
      mock.setSomethingElse("test");
      mock.save();

      new Verifications() {{
         mock.save(); times = 2;
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyInvocationsWithInvocationCountLargerThanOccurred()
   {
      mock.setSomethingElse("test");
      mock.setSomething(3);
      mock.save();

      new Verifications() {{
         mock.setSomething(anyInt);
         times = 3;
      }};
   }

   @Test
   public void verifySimpleInvocationsInIteratingBlock()
   {
      mock.setSomething(123);
      mock.save();
      mock.setSomething(45);
      mock.save();

      new Verifications(2) {{
         mock.setSomething(anyInt);
         mock.save();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifySingleInvocationInBlockWithLargerNumberOfIterations()
   {
      mock.setSomething(123);

      new Verifications(3) {{ mock.setSomething(anyInt); }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyMultipleInvocationsInBlockWithSmallerNumberOfIterations()
   {
      mock.setSomething(45);
      mock.setSomething(123);
      mock.setSomething(-1015);

      new Verifications(2) {{
         mock.setSomething(anyInt);
      }};
   }

   @Test
   public void verifyWithArgumentMatcher()
   {
      exerciseCodeUnderTest();

      new Verifications() {{ mock.setSomething(anyInt); }};
   }

   @Test
   public void verifyWithArgumentMatcherAndIndividualInvocationCounts()
   {
      exerciseCodeUnderTest();

      new Verifications(1) {{
         mock.prepare(); maxTimes = 1;
         mock.setSomething(anyInt); minTimes = 2;
         mock.editABunchMoreStuff(); minTimes = 0; maxTimes = 5;
         mock.save(); times = 1;
      }};
   }

   @Test
   public void verifyWithCustomArgumentMatcherWithoutArgumentValue()
   {
      mock.setSomethingElse("not empty");

      new Verifications() {{
         mock.setSomethingElse(with(new Delegate<String>() {
            boolean isNotEmpty(String s) { return s.length() > 0; }
         }));
      }};
   }

   @Test(expected = AssertionError.class)
   public void verifyWithValidationMethod()
   {
      mock.setSomethingElse("test");

      new Verifications() {{
         mock.setSomethingElse(anyString);
         forEachInvocation = new Object() { void assertEmpty(String s) { assertEquals(0, s.length()); } };
      }};
   }

   @Test(expected = AssertionError.class)
   public void verifyWithValidationMethodWhichReturnsBoolean()
   {
      mock.setSomethingElse("test");

      new Verifications() {{
         mock.setSomethingElse(anyString);
         forEachInvocation = new Object() { boolean isEmpty(String s) { return s.length() == 0; } };
      }};
   }

   @Test
   public void verifyWithArgumentMatcherAndIndividualInvocationCountsInIteratingBlock()
   {
      for (int i = 0; i < 2; i++) {
         exerciseCodeUnderTest();
      }

      new Verifications(2) {{
         mock.prepare(); maxTimes = 1;
         mock.setSomething(anyInt); minTimes = 2;
         mock.editABunchMoreStuff(); minTimes = 0; maxTimes = 5;
         mock.save(); times = 1;
      }};
   }

   @Test
   public void verifyInvocationsToPrivateMethodsAndConstructors()
   {
      new Dependency(9).privateMethod();
      Dependency.privateStaticMethod("test", true);

      new Verifications() {{
         newInstance(Dependency.class.getName(), 9);
         invoke(mock, "privateMethod");
         invoke(Dependency.class, "privateStaticMethod", "test", true);
      }};
   }

   @Ignore @Test(expected = AssertionError.class)
   public void verifyTwoInvocationsWithIteratingBlockHavingExpectationRecordedAndSecondInvocationUnverified()
   {
      new NonStrictExpectations() {{ mock.setSomething(anyInt); }};

      mock.setSomething(123);
      mock.setSomething(45);

      new Verifications(2) {{ mock.setSomething(123); }};
   }
}
