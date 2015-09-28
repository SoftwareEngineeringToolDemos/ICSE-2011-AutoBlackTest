/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

import mockit.internal.util.*;

@SuppressWarnings({
   "ObjectEqualsNull", "EqualsBetweenInconvertibleTypes", "LiteralAsArgToStringEquals", "FinalizeCalledExplicitly", "SimplifiableJUnitAssertion"})
public final class ObjectOverridesTest
{
   @Test
   public void verifyStandardBehaviorOfOverridableObjectMethodsInMockedInterface(Runnable a, Runnable b)
   {
      assertDefaultEqualsBehavior(a, b);
      assertDefaultEqualsBehavior(b, a);

      assertDefaultHashCodeBehavior(a);
      assertDefaultHashCodeBehavior(b);

      assertDefaultToStringBehavior(a);
      assertDefaultToStringBehavior(b);
   }

   private void assertDefaultEqualsBehavior(Object a, Object b)
   {
      assertFalse(a.equals(null));
      assertFalse(a.equals("test"));
      assertTrue(a.equals(a));
      assertFalse(a.equals(b));
   }

   private void assertDefaultHashCodeBehavior(Object a)
   {
      assertEquals(System.identityHashCode(a), a.hashCode());
   }

   private void assertDefaultToStringBehavior(Object a)
   {
      assertEquals(Utilities.objectIdentity(a), a.toString());
   }

   @Test
   public void verifyStandardBehaviorOfOverriddenObjectMethodsInMockedJREClass(Date a, Date b)
   {
      assertDefaultEqualsBehavior(a, b);
      assertDefaultEqualsBehavior(b, a);

      assertDefaultHashCodeBehavior(a);
      assertDefaultHashCodeBehavior(b);

      assertDefaultToStringBehavior(a);
      assertDefaultToStringBehavior(b);
   }

   @Mocked ClassWithObjectOverrides a;
   @Mocked ClassWithObjectOverrides b;

   @Before
   public void callObjectMethodsInMockBeforeEveryTest()
   {
      assertEquals(System.identityHashCode(a), a.hashCode());
      assertEquals(b, b);
   }

   @Test
   public void verifyStandardBehaviorOfOverriddenObjectMethodsInMockedClass()
   {
      assertDefaultEqualsBehavior(a, b);
      assertDefaultEqualsBehavior(b, a);

      assertDefaultHashCodeBehavior(a);
      assertDefaultHashCodeBehavior(b);

      assertDefaultToStringBehavior(a);
      assertDefaultToStringBehavior(b);

      a.finalize();
      b.finalize();
   }

   @Test
   public void mockOverrideOfEqualsMethod()
   {
      new Expectations() {{
         a.equals(null); result = true;
         a.equals(anyString); result = true;
      }};

      new NonStrictExpectations() {{
         b.equals(a); result = true;
      }};

      assertTrue(a.equals(null));
      assertTrue(a.equals("test"));
      assertTrue(b.equals(a));
   }

   @Test
   public void mockOverrideOfHashCodeMethod()
   {
      assertTrue(a.hashCode() != b.hashCode());

      new NonStrictExpectations() {{
         a.hashCode(); result = 123;
         b.hashCode(); result = 45; times = 1;
      }};

      assertEquals(123, a.hashCode());
      assertEquals(45, b.hashCode());
   }

   @Test
   public void mockOverrideOfToStringMethod()
   {
      assertFalse(a.toString().equals(b.toString()));

      new NonStrictExpectations() {{
         a.toString(); result = "mocked";
      }};

      assertTrue("mocked".equals(a.toString()));

      new Verifications() {{
         a.toString();
         b.toString(); times = 1;
      }};
   }

   @Test
   public void mockOverrideOfCloneMethod()
   {
      new Expectations() {{
         a.clone(); result = b;
      }};

      assertSame(b, a.clone());
   }

   @Test
   public void allowAnyInvocationsOnOverriddenObjectMethodsForStrictMocks()
   {
      new Expectations() {{
         a.getIntValue(); result = 58;
         b.doSomething();
      }};

      assertFalse(a.equals(b));
      assertTrue(a.hashCode() != b.hashCode());
      assertEquals(58, a.getIntValue());
      assertTrue(a.equals(a));
      String bStr = b.toString();
      b.doSomething();
      assertFalse(b.equals(a));
      String aStr = a.toString();
      assertFalse(aStr.equals(bStr));

      new Verifications() {{
         a.equals(b);
         b.hashCode(); times = 1;
         a.toString();
         b.equals(null); times = 0;
      }};

      new VerificationsInOrder() {{
         a.hashCode();
         b.equals(a);
      }};
   }

   @Test
   public void recordExpectationsOnOverriddenObjectMethodAsNonStrictEvenInsideStrictExpectationBlock()
   {
      new Expectations() {{
         a.doSomething();
         a.hashCode(); result = 1;
         a.equals(any);
         a.toString();
      }};

      a.doSomething();
   }
}
