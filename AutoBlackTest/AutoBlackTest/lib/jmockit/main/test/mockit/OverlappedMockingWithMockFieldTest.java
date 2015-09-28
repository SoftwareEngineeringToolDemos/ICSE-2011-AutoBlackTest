/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

public final class OverlappedMockingWithMockFieldTest
{
   static final class DerivedClass extends BaseClass
   {
      boolean doSomethingElse() { doSomething1(); return true; }
   }

   @BeforeClass @AfterClass
   public static void verifyNoMockingBeforeAndAfterAllTests()
   {
      BaseClass base = new BaseClass();
      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      try { BaseClass.doStatic1(); fail(); } catch (RuntimeException ignore) {}
      try { BaseClass.doStatic2(); fail(); } catch (RuntimeException ignore) {}
   }

   @Mocked("doSomething1") BaseClass base;

   @Test
   public void overlappedStaticPartialMocking(@Mocked({"doSomething2", "doSomethingElse"}) final DerivedClass derived)
   {
      new NonStrictExpectations() {{
         derived.doSomethingElse(); result = true;
      }};

      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      base.doSomething2();

      try { derived.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      derived.doSomething2();
      assertTrue(derived.doSomethingElse());
   }

   @Test
   public void overlappedStaticPartialMockingWithLocalMockFieldInsteadOfMockParameter()
   {
      assertRegularMockingOfBaseClass();

      DerivedClass derived = new DerivedClass();

      new NonStrictExpectations() {
         @Mocked({"doSomething2", "doSomethingElse"}) DerivedClass mock;

         {
            mock.doSomethingElse(); result = true;
         }
      };

      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      base.doSomething2();

      try { derived.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      derived.doSomething2();
      assertTrue(derived.doSomethingElse());
   }

   @Test
   public void regularMockingOfBaseClassAfterRegularMockingOfDerivedClassInPreviousTest()
   {
      assertRegularMockingOfBaseClass();
   }

   private void assertRegularMockingOfBaseClass()
   {
      base.doSomething1();
      try { base.doSomething2(); fail(); } catch (RuntimeException ignore) {}

      DerivedClass derived = new DerivedClass();
      assertTrue(derived.doSomethingElse());
      derived.doSomething1();
      try { derived.doSomething2(); fail(); } catch (RuntimeException ignore) {}
   }

   @Test
   public void overlappedDynamicPartialMockingOfAllInstances()
   {
      final DerivedClass derived = new DerivedClass();

      new NonStrictExpectations(DerivedClass.class) {{
         base.doSomething2();
         derived.doSomethingElse(); result = true;
      }};

      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      base.doSomething2();

      try { derived.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      derived.doSomething2();
      assertTrue(derived.doSomethingElse());

      new Verifications() {{
         base.doSomething1(); times = 2;
         base.doSomething2(); times = 2;
         derived.doSomething1(); times = 2;
         derived.doSomething2(); times = 2;
         derived.doSomethingElse(); times = 1;
      }};
   }

   @Test
   public void regularMockingOfBaseClassAfterDynamicMockingOfDerivedClassInPreviousTest()
   {
      assertRegularMockingOfBaseClass();
   }

   @Test
   public void overlappedDynamicPartialMockingOfSingleInstance()
   {
      final DerivedClass derived = new DerivedClass();

      new NonStrictExpectations(derived) {{
         derived.doSomething2();
         derived.doSomethingElse(); result = true;
      }};

      try { base.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      try { base.doSomething2(); fail(); } catch (RuntimeException ignore) {}

      try { derived.doSomething1(); fail(); } catch (RuntimeException ignore) {}
      derived.doSomething2();
      assertTrue(derived.doSomethingElse());

      new Verifications() {{
         base.doSomething1(); times = 1;
         base.doSomething2(); times = 1;
         derived.doSomething1(); times = 1;
         derived.doSomething2(); times = 1;
         derived.doSomethingElse(); times = 1;
      }};
   }

   @Test
   public void regularMockingOfBaseClassAfterDynamicMockingOfDerivedClassInstanceInPreviousTest()
   {
      assertRegularMockingOfBaseClass();
   }
}
