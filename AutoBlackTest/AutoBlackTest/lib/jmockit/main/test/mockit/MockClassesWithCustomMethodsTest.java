/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

public final class MockClassesWithCustomMethodsTest
{
   private final CodeUnderTest codeUnderTest = new CodeUnderTest();

   static class CodeUnderTest
   {
      int doSomethingWithInnerDependency()
      {
         return new IDependency() {
            public int doSomething() { return 45; }
         }.doSomething();
      }
   }

   public interface IDependency { int doSomething(); }

   static class Collaborator
   {
      protected int value;
      int getValue() { return value; }
      void setValue(int value) { this.value = value; }
   }

   static final class SubCollaborator1 extends Collaborator
   {
      @Override
      int getValue() { return 45; }
   }

   static final class SubCollaborator2 extends Collaborator
   {
      @Override
      int getValue() { return 46; }
   }

   @BeforeClass
   public static void setUpAllTests()
   {
      new MockUp<Runnable>() {
         @Override
         protected boolean shouldBeMocked(ClassLoader loader, String subclassName)
         {
            return loader == ClassLoader.getSystemClassLoader() && subclassName.endsWith("TestAction");
         }

         @Mock void run() {}
      };
   }

   @Before
   public void useClassesToBeCapturedForAllTests()
   {
      class TestAction implements Runnable
      {
         public void run() { throw new RuntimeException("Not to happen"); }
      }
      new TestAction().run();
   }

   int specialMethodInvocationCount;

   @Test
   public void mockSubclassAlreadyLoaded()
   {
      SubCollaborator1 subCollaborator = new SubCollaborator1();

      new MockUp<Collaborator>() {
         @Override
         protected boolean shouldBeMocked(ClassLoader cl, String subclassName)
         {
            assertSame(ClassLoader.getSystemClassLoader(), cl);
            assertTrue(subclassName.contains("SubCollaborator"));
            specialMethodInvocationCount++;
            return true;
         }

         @Mock
         int getValue() { return 123; }
      };

      assertEquals(1, specialMethodInvocationCount);
      assertEquals(123, new Collaborator().getValue());
      assertEquals(123, subCollaborator.getValue());
      assertEquals(1, specialMethodInvocationCount);
   }

   @Ignore @Test
   public void mockSubclassWhenLoaded()
   {
      new SubCollaborator1();

      new MockUp<Collaborator>() {
         int i;

         @Override
         protected boolean shouldBeMocked(ClassLoader cl, String subclassName)
         {
            assertTrue(i < 2);
            assertSame(ClassLoader.getSystemClassLoader(), cl);
            i++;
            assertEquals(MockClassesWithCustomMethodsTest.class.getName() + "$SubCollaborator" + i, subclassName);
            specialMethodInvocationCount++;
            return true;
         }

         @Mock
         int getValue() { return 123; }
      };

      assertEquals(123, new Collaborator().getValue());
      assertEquals(1, specialMethodInvocationCount);

      assertEquals(123, new SubCollaborator2().getValue());
      assertEquals(2, specialMethodInvocationCount);
   }

   @Ignore @Test
   public void mockClassImplementingInterfaceWhenLoaded()
   {
      new MockUp<IDependency>() {
         @Override
         protected boolean shouldBeMocked(ClassLoader cl, String subclassName)
         {
            assertNotNull(cl);
            assertEquals(CodeUnderTest.class.getName() + "$1", subclassName);
            specialMethodInvocationCount++;
            return true;
         }

         @Mock(invocations = 1) int doSomething() { return 56; }
      };

      assertEquals(0, specialMethodInvocationCount);
      assertEquals(56, codeUnderTest.doSomethingWithInnerDependency());
      assertEquals(1, specialMethodInvocationCount);
   }

   @After
   public void verifyThatCapturedClassIsNoLongerMocked()
   {
      assertEquals(45, new SubCollaborator1().getValue());
   }
}
