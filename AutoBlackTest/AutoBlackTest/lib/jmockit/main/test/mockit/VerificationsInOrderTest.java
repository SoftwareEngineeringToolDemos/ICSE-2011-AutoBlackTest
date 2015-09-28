/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import org.junit.*;

import mockit.internal.*;

@SuppressWarnings("UnusedDeclaration")
public final class VerificationsInOrderTest
{
   public static class Dependency
   {
      public void setSomething(int value) {}
      public void setSomethingElse(String value) {}
      public void editABunchMoreStuff() {}
      public void notifyBeforeSave() {}
      public void prepare() {}
      public void save() {}
      void doSomething(ClassWithHashCode h) {}
   }

   static final class ClassWithHashCode
   {
      @Override
      public boolean equals(Object obj) { return obj instanceof ClassWithHashCode && this == obj; }

      @Override
      public int hashCode() { return 123; }
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

      new VerificationsInOrder() {{
         mock.prepare();
         mock.setSomething(45);
         mock.editABunchMoreStuff();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyUnrecordedInvocationThatShouldHappenButDoesNot()
   {
      mock.setSomething(1);

      new VerificationsInOrder() {{ mock.notifyBeforeSave(); }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyUnrecordedInvocationThatShouldHappenExactlyOnceButDoesNot()
   {
      mock.setSomething(1);

      new VerificationsInOrder() {{ mock.setSomething(2); times = 1; }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyRecordedInvocationThatShouldHappenButDoesNot()
   {
      new NonStrictExpectations() {{
         mock.setSomething(1);
         mock.notifyBeforeSave();
      }};

      mock.setSomething(1);

      new VerificationsInOrder() {{
         mock.setSomething(1);
         mock.notifyBeforeSave();
      }};
   }

   @Test
   public void verifyAllInvocationsWithSomeOfThemRecorded()
   {
      new NonStrictExpectations() {{
         mock.prepare();
         mock.editABunchMoreStuff();
      }};

      exerciseCodeUnderTest();

      new VerificationsInOrder() {{
         mock.prepare(); minTimes = 1;
         mock.setSomethingElse(anyString);
         mock.setSomething(anyInt); minTimes = 1; maxTimes = 2;
         mock.editABunchMoreStuff();
         mock.notifyBeforeSave(); maxTimes = 1;
         mock.save(); times = 1;
      }};
   }

   @Test
   public void verifyInvocationsWithOneRecordedButNotReplayed()
   {
      new NonStrictExpectations() {{
         mock.prepare();
         mock.editABunchMoreStuff();
      }};

      mock.prepare();
      mock.setSomething(123);
      mock.setSomethingElse("a");
      mock.save();

      new VerificationsInOrder() {{
         mock.setSomething(anyInt);
         mock.setSomethingElse(anyString);
         mock.save();
      }};
   }

   @Ignore @Test
   public void verifyInvocationsWithExactInvocationCountsHavingRecordedMatchingExpectationWithArgumentMatcher()
   {
      new NonStrictExpectations() {{ mock.setSomething(anyInt); }};

      mock.setSomething(1);
      mock.setSomething(2);

      new VerificationsInOrder() {{
         mock.setSomething(1); times = 1;
         mock.setSomething(2); times = 1;
      }};
   }

   @Test
   public void verifyInvocationThatIsAllowedToHappenAnyNumberOfTimesAndHappensOnce()
   {
      mock.prepare();
      mock.setSomething(123);
      mock.save();

      new VerificationsInOrder() {{
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

      new VerificationsInOrder() {{
         mock.prepare();
         mock.save(); minTimes = 0;
      }};
   }

   @Test
   public void verifyUnrecordedInvocationThatIsAllowedToHappenAnyNoOfTimesAndDoesNotHappen()
   {
      mock.prepare();
      mock.setSomething(123);

      new VerificationsInOrder() {{
         mock.setSomething(anyInt);
         mock.save(); minTimes = 0;
      }};
   }

   @Test
   public void verifyIntermediateUnrecordedInvocationThatDoesNotHappenButCould()
   {
      mock.prepare();
      mock.setSomething(123);

      new VerificationsInOrder() {{
         mock.prepare();
         mock.editABunchMoreStuff(); minTimes = 0;
         mock.setSomething(anyInt);
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifySimpleInvocationsWhenOutOfOrder()
   {
      mock.setSomething(123);
      mock.prepare();

      new VerificationsInOrder() {{
         mock.prepare();
         mock.setSomething(123);
      }};
   }

   @Test
   public void verifyRepeatingInvocation()
   {
      mock.setSomething(123);
      mock.setSomething(123);

      new VerificationsInOrder() {{ mock.setSomething(123); times = 2; }};
   }

   @Test(expected = UnexpectedInvocation.class)
   public void verifyRepeatingInvocationThatOccursOneTimeMoreThanExpected()
   {
      mock.setSomething(123);
      mock.setSomething(123);

      new VerificationsInOrder() {{ mock.setSomething(123); maxTimes = 1; }};
   }

   @Test
   public void verifySimpleInvocationInIteratingBlock()
   {
      mock.setSomething(123);
      mock.setSomething(123);

      new VerificationsInOrder(2) {{ mock.setSomething(123); }};
   }

   @Test
   public void verifyRepeatingInvocationInIteratingBlock()
   {
      mock.setSomething(123);
      mock.setSomething(123);
      mock.setSomething(123);
      mock.setSomething(123);

      new VerificationsInOrder(2) {{ mock.setSomething(123); minTimes = 2; }};
   }

   @Test
   public void verifyRepeatingInvocationUsingMatcher()
   {
      mock.setSomething(123);
      mock.setSomething(45);

      new VerificationsInOrder() {{ mock.setSomething(anyInt); times = 2; }};
   }

   @Test
   public void verifySimpleInvocationInIteratingBlockUsingMatcher()
   {
      mock.setSomething(123);
      mock.setSomething(45);

      new VerificationsInOrder(2) {{ mock.setSomething(anyInt); }};
   }

   @Test
   public void verifySimpleInvocationsInIteratingBlock()
   {
      mock.setSomething(123);
      mock.save();
      mock.setSomething(45);
      mock.save();

      new VerificationsInOrder(2) {{
         mock.setSomething(anyInt);
         mock.save();
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifySingleInvocationInBlockWithLargerNumberOfIterations()
   {
      mock.setSomething(123);

      new VerificationsInOrder(3) {{ mock.setSomething(123); }};
   }

   @Test
   public void verifyMultipleInvocationsInBlockWithSmallerNumberOfIterations()
   {
      mock.setSomething(-67);
      mock.setSomething(123);
      mock.setSomething(45);

      new VerificationsInOrder(2) {{ mock.setSomething(anyInt); }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyMultipleInvocationsInIteratingBlockContainingDuplicateVerificationThatCannotBeSatisfied()
   {
      mock.setSomething(-67);
      mock.setSomething(123);
      mock.setSomething(45);

      new VerificationsInOrder(2) {{
         mock.setSomething(anyInt);
         mock.setSomething(anyInt);
      }};
   }

   @Test
   public void verifyWithArgumentMatcher()
   {
      exerciseCodeUnderTest();

      new VerificationsInOrder() {{
         mock.prepare();
         mock.setSomething(anyInt);
      }};
   }

   @Test
   public void verifyWithIndividualInvocationCountsForNonConsecutiveInvocations()
   {
      exerciseCodeUnderTest();

      new VerificationsInOrder() {{
         mock.prepare(); times = 1;
         mock.setSomething(anyInt); times = 2;
      }};
   }

   @Test
   public void verifyUsingInvocationCountConstraintAndArgumentMatcherOnObjectWithMockedHashCode(ClassWithHashCode wh)
   {
      mock.doSomething(null);
      mock.doSomething(wh);

      new VerificationsInOrder() {{
         mock.doSomething((ClassWithHashCode) withNull()); times = 1;
         mock.doSomething((ClassWithHashCode) withNotNull());
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyWithArgumentMatchersWhenOutOfOrder()
   {
      mock.setSomething(123);
      mock.setSomethingElse("anotherValue");
      mock.setSomething(45);

      new VerificationsInOrder() {{
         mock.setSomething(anyInt);
         mock.setSomething(anyInt);
         mock.setSomethingElse(anyString);
      }};
   }

   @Test(expected = MissingInvocation.class)
   public void verifyWithArgumentMatcherAndIndividualInvocationCountWhenOutOfOrder()
   {
      mock.setSomething(123);
      mock.prepare();
      mock.setSomething(45);

      new VerificationsInOrder() {{
         mock.prepare();
         mock.setSomething(anyInt); times = 2;
      }};
   }

   @Test
   public void verifyTwoIndependentSequencesOfInvocationsWhichOccurSeparately()
   {
      // First sequence:
      mock.setSomething(1);
      mock.setSomething(2);

      // Second sequence:
      mock.setSomething(10);
      mock.setSomething(20);

      // Verifies first sequence:
      new VerificationsInOrder() {{
         mock.setSomething(1);
         mock.setSomething(2);
      }};

      // Verifies second sequence:
      new VerificationsInOrder() {{
         mock.setSomething(10);
         mock.setSomething(20);
      }};
   }

   @Test
   public void verifyTwoIndependentSequencesOfInvocationsWhichAreMixedTogether()
   {
      mock.setSomething(1);  // first sequence
      mock.setSomething(10); // second sequence
      mock.setSomething(2);  // first sequence
      mock.setSomething(20); // second sequence

      // Verifies second sequence:
      new VerificationsInOrder() {{
         mock.setSomething(10);
         mock.setSomething(20);
      }};

      // Verifies first sequence:
      new VerificationsInOrder() {{
         mock.setSomething(1);
         mock.setSomething(2);
      }};
   }

   @Test
   public void verifySecondSequenceOfInvocationsWithTimesConstraintAfterVerifyingLastInvocationOfFirstSequence()
   {
      mock.setSomething(1); // first sequence
      mock.setSomething(3); // second sequence
      mock.setSomething(4); // second sequence
      mock.setSomething(2); // first sequence

      new VerificationsInOrder() {{
         mock.setSomething(1);
         mock.setSomething(2);
      }};

      new VerificationsInOrder() {{
         mock.setSomething(3);
         mock.setSomething(4); times = 1;
      }};
   }
}
