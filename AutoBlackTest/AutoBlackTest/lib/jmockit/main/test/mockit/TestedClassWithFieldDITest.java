/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

public final class TestedClassWithFieldDITest
{
   public static class TestedClass
   {
      protected final int i;

      // Suppose this is injected by some DI framework or Java EE container:
      @SuppressWarnings("UnusedDeclaration") protected Dependency dependency;

      public TestedClass() { i = -1; }
      public TestedClass(int i) { this.i = i; }

      public boolean doSomeOperation() { return dependency.doSomething() > 0; }
   }

   static class Dependency { int doSomething() { return -1; } }

   @Tested TestedClass tested;
   @Injectable Dependency dependency;

   @Test
   public void exerciseTestedObjectWithFieldInjectedByType()
   {
      assertEquals(-1, tested.i);
      assertSame(dependency, tested.dependency);

      new NonStrictExpectations() {{
         dependency.doSomething(); result = 23; times = 1;
      }};

      assertTrue(tested.doSomeOperation());
   }

   @Test
   public void exerciseTestedObjectCreatedThroughConstructorAndFieldInjection(@Injectable("123") int value)
   {
      assertEquals(123, tested.i);
      assertSame(dependency, tested.dependency);
   }
}
