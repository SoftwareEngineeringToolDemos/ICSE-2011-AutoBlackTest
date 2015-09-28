/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import org.junit.*;

import static org.junit.Assert.*;

public final class ExpectationsForConstructorsTest
{
   public static class BaseCollaborator
   {
      protected int value;

      protected BaseCollaborator() { value = -1; }
      protected BaseCollaborator(int value) { this.value = value; }

      protected boolean add(Integer i) { return i != null; }
   }

   static class Collaborator extends BaseCollaborator
   {
      Collaborator() {}
      Collaborator(int value) { super(value); }
   }

   @SuppressWarnings("UnusedDeclaration")
   public abstract static class AbstractCollaborator extends BaseCollaborator
   {
      protected AbstractCollaborator(int value) { super(value); }
      protected AbstractCollaborator(boolean b, int value) { super(value); }

      protected abstract void doSomething();
   }

   @Test
   public void mockAllConstructors()
   {
      new Expectations() {
         @Mocked final Collaborator unused = null;

         {
            new Collaborator();
            new Collaborator(123);
         }
      };

      assertEquals(0, new Collaborator().value);
      assertEquals(0, new Collaborator(123).value);
   }

   @Test
   public void mockOnlyOneConstructor()
   {
      new Expectations() {
         @Mocked("(int)")
         final Collaborator unused = null;

         {
            new Collaborator(123);
         }
      };

      assertEquals(-1, new Collaborator().value);
      assertEquals(-1, new Collaborator(123).value);
   }

   @Test
   public void mockOnlyNoArgsConstructor()
   {
      new Expectations() {
         @Mocked("()")
         final Collaborator unused = null;

         {
            new Collaborator();
         }
      };

      assertEquals(0, new Collaborator().value);
      assertEquals(123, new Collaborator(123).value);
   }

   @Test
   public void partiallyMockAbstractClass(final AbstractCollaborator mock)
   {
      new Expectations() {{ mock.doSomething(); }};

      mock.doSomething();
   }

   @Test
   public void partiallyMockSubclass()
   {
      new Expectations() {
         @Mocked("add") Collaborator mock;

         {
            mock.add(5); result = false;
         }
      };

      assertEquals(12, new Collaborator(12).value);
      assertFalse(new Collaborator().add(5));
   }

   static class A
   {
      @SuppressWarnings("UnusedDeclaration") private A() {}
      A(String s) { assertNotNull("A(String) executed with null", s); }
   }
   static class B extends A { B(String s) { super(s); } }

   @Test
   public void mockClassHierarchyWhereFirstConstructorInBaseClassIsPrivate(@Mocked B mock)
   {
      new A(null);
      new B("Test1");
   }

   @SuppressWarnings({"UnnecessaryFullyQualifiedName", "UnusedParameters"})
   static class D extends integrationTests.C { D(String s) {} }

   @Test
   public void mockClassHierarchyWhereFirstConstructorInBaseClassOnAnotherPackageIsPackagePrivate(D mock)
   {
      assertNotNull(mock);
      new D("Test1");
   }
}
