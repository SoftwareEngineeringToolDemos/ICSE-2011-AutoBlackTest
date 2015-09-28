/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.*;

public final class CascadingFieldTest
{
   static class Foo
   {
      Bar getBar() { return null; }

      static Bar globalBar() { return null; }

      void doSomething(String s) { throw new RuntimeException(s); }
      int getIntValue() { return 1; }
      private Boolean getBooleanValue() { return true; }
      String getStringValue() { return "abc"; }
      public final Date getDate() { return null; }
      final List<Integer> getList() { return null; }
   }

   static class Bar
   {
      Bar() { throw new RuntimeException(); }
      int doSomething() { return 1; }
      boolean isDone() { return false; }
      Short getShort() { return 1; }
      List<?> getList() { return null; }
      Baz getBaz() { return null; }
      Runnable getTask() { return null; }
   }

   static final class Baz { void doSomething() {} }

   public interface A { B getB(); }
   public interface B { C getC(); }
   public interface C {}

   @Cascading Foo foo;
   @Cascading A a;

   @Before
   public void recordCommonExpectations()
   {
      new NonStrictExpectations() {{
         foo.getBar().isDone(); result = true;
      }};
   }

   @Test
   public void obtainCascadedInstancesAtAllLevels()
   {
      assertNotNull(foo.getBar());
      assertNotNull(foo.getBar().getList());
      assertNotNull(foo.getBar().getBaz());
      assertNotNull(foo.getBar().getTask());

      B b = a.getB();
      assertNotNull(b);
      assertNotNull(b.getC());
   }

   @Test
   public void obtainCascadedInstancesAtAllLevelsAgain()
   {
      Bar bar = foo.getBar();
      assertNotNull(bar);
      assertNotNull(bar.getList());
      assertNotNull(bar.getBaz());
      assertNotNull(bar.getTask());

      assertNotNull(a.getB());
      assertNotNull(a.getB().getC());
   }

   @Test
   public void cascadeOneLevel()
   {
      assertTrue(foo.getBar().isDone());
      assertEquals(0, foo.getBar().doSomething());
      assertEquals(0, Foo.globalBar().doSomething());
      assertNotSame(foo.getBar(), Foo.globalBar());
      assertNull(foo.getBar().getShort());

      foo.doSomething("test");
      assertEquals(0, foo.getIntValue());
      assertNull(foo.getBooleanValue());
      assertNull(foo.getStringValue());
      assertNotNull(foo.getDate());
      assertTrue(foo.getList().isEmpty());

      new Verifications() {{ foo.doSomething(anyString); }};
   }

   @Test
   public void exerciseCascadingMockAgain()
   {
      assertTrue(foo.getBar().isDone());
   }

   @Test
   public void recordUnambiguousStrictExpectationsProducingDifferentCascadedInstances()
   {
      new Expectations() {{
         Bar c1 = Foo.globalBar();
         c1.isDone(); result = true;
         Bar c2 = Foo.globalBar();
         c2.doSomething(); result = 5;
         assertNotSame(c1, c2);
      }};

      Bar b1 = Foo.globalBar();
      assertTrue(b1.isDone());
      Bar b2 = Foo.globalBar();
      assertEquals(5, b2.doSomething());
      assertNotSame(b1, b2);
   }

   @Test
   public void recordUnambiguousNonStrictExpectationsProducingDifferentCascadedInstances(
      @Cascading final Foo foo1, @Cascading final Foo foo2)
   {
      new NonStrictExpectations() {{
         Date c1 = foo1.getDate();
         Date c2 = foo2.getDate();
         assertNotSame(c1, c2);
      }};

      Date d1 = foo1.getDate();
      Date d2 = foo2.getDate();
      assertNotSame(d1, d2);
   }

   @Test
   public void recordAmbiguousNonStrictExpectationsOnInstanceMethodProducingTheSameCascadedInstance()
   {
      new NonStrictExpectations() {{
         Bar c1 = foo.getBar();
         Bar c2 = foo.getBar();
         assertSame(c1, c2);
      }};

      Bar b1 = foo.getBar();
      Bar b2 = foo.getBar();
      assertSame(b1, b2);
   }

   @Test
   public void recordAmbiguousNonStrictExpectationsOnStaticMethodProducingTheSameCascadedInstance()
   {
      new NonStrictExpectations() {{
         Bar c1 = Foo.globalBar();
         Bar c2 = Foo.globalBar();
         assertSame(c1, c2);
      }};

      Bar b1 = Foo.globalBar();
      Bar b2 = Foo.globalBar();
      assertSame(b1, b2);
   }

   static class Dependency { static Dependency create() { return null; } }
   static class Dependent
   {
      private static final Dependency DEPENDENCY = Dependency.create();
      static { DEPENDENCY.toString(); }
   }

   @Mocked Dependent dependent;
   @Cascading Dependency dependency;

   static final class AnotherFoo { Bar getBar() { return null; } }

   @Test
   public void cascadingLocalMockField()
   {
      new NonStrictExpectations() {
         @Cascading AnotherFoo anotherFoo;

         {
            anotherFoo.getBar().doSomething(); result = 123;
         }
      };

      assertEquals(123, new AnotherFoo().getBar().doSomething());
   }

   @Test
   public void cascadingInstanceAccessedFromDelegateMethod()
   {
      new NonStrictExpectations() {{
         foo.getIntValue();
         result = new Delegate() {
            int delegate() { return foo.getBar().doSomething(); }
         };
      }};

      assertEquals(0, foo.getIntValue());
   }

   // Tests for cascaded instances obtained from generic methods //////////////////////////////////////////////////////

   static class GenericBaseClass1<T> { T getValue() { return null; } }

   @Ignore @Test
   public void cascadeGenericMethodFromSpecializedGenericClass(@Cascading GenericBaseClass1<A> mock)
   {
      A value = mock.getValue();
      assertNotNull(value);
   }

   static class ConcreteSubclass1 extends GenericBaseClass1<A> {}

   @Ignore @Test
   public void cascadeGenericMethodOfConcreteSubclassWhichExtendsGenericClass(@Cascading ConcreteSubclass1 mock)
   {
      A value = mock.getValue();
      assertNotNull(value);
   }

   interface Ab extends A {}
   static class GenericBaseClass2<T extends A> { T getValue() { return null; } }
   static class ConcreteSubclass2 extends GenericBaseClass2<Ab> {}

   @Ignore @Test
   public void cascadeGenericMethodOfConcreteSubclassWhichExtendsGenericClassWithUpperBound(
      @Cascading ConcreteSubclass2 mock)
   {
      Ab value = mock.getValue();
      assertNotNull(value);
   }
}
