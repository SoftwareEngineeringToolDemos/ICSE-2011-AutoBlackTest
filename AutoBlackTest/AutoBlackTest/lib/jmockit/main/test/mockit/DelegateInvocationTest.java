/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

@SuppressWarnings({"UnusedDeclaration"})
public final class DelegateInvocationTest
{
   static class Collaborator
   {
      Collaborator() {}
      Collaborator(int i) {}

      int getValue() { return -1; }
      String doSomething(boolean b, int[] i, String s) { return s + b + i[0]; }
      static boolean staticMethod() { return true; }
      static boolean staticMethod(int i) { return i > 0; }
      native long nativeMethod(boolean b);
      final char finalMethod() { return 's'; }
      private float privateMethod() { return 1.2F; }
      void addElements(Collection<String> elements) { elements.add("one element"); }
   }

   // In Delegate methods /////////////////////////////////////////////////////////////////////////////////////////////

   @Test
   public void delegateWithContextObject()
   {
      new NonStrictExpectations() {
         final Collaborator unused = null;

         {
            Collaborator.staticMethod();
            returns(new Delegate() {
               boolean staticMethod(Invocation context)
               {
                  assertNull(context.getInvokedInstance());
                  assertEquals(context.getInvocationCount() - 1, context.getInvocationIndex());
                  assertEquals(0, context.getMinInvocations());
                  assertEquals(-1, context.getMaxInvocations());
                  return context.getInvocationCount() > 0;
               }
            });
         }
      };

      assertTrue(Collaborator.staticMethod());
      assertTrue(Collaborator.staticMethod());
   }

   static class ConstructorDelegate implements Delegate
   {
      int capturedArgument;

      void $init(Invocation context, int i)
      {
         assertNotNull(context.getInvokedInstance());
         capturedArgument = i + context.getInvocationCount();
      }
   }

   @Test
   public void delegateForConstructorWithContext()
   {
      final ConstructorDelegate delegate = new ConstructorDelegate();

      new Expectations() {
         Collaborator mock;

         {
            new Collaborator(anyInt); result = delegate;
         }
      };

      new Collaborator(4);

      assertEquals(5, delegate.capturedArgument);
   }

   @Test
   public void delegateReceivingNullArguments()
   {
      final Collaborator collaborator = new Collaborator();

      new NonStrictExpectations() {
         Collaborator mock;

         {
            mock.doSomething(true, null, null);
            result = new Delegate() {
               void doSomething(Invocation invocation, Boolean b, int[] i, String s)
               {
                  Collaborator instance = invocation.getInvokedInstance();
                  assertSame(collaborator, instance);
                  assertEquals(1, invocation.getInvocationCount());
                  assertTrue(b);
                  assertNull(i);
                  assertNull(s);
               }
            };
         }
      };

      assertNull(collaborator.doSomething(true, null, null));
   }

   @Test
   public void delegateWithAnotherMethodOnTheDelegateClass()
   {
      new NonStrictExpectations() {
         Collaborator mock;

         {
            mock.getValue();
            result = new Delegate() {
               int getValue(Invocation context)
               {
                  return context.getInvocationCount();
               }

               void otherMethod(Invocation context)
               {
                  fail();
               }
            };
         }
      };

      assertEquals(1, new Collaborator().getValue());
      assertEquals(2, new Collaborator().getValue());
   }

   @Test
   public void delegateClassWithMultipleMethodsAndInexactButValidMatch()
   {
      new Expectations() {
         Collaborator mock;

         {
            Collaborator.staticMethod(1);
            result = new Delegate() {
               void otherMethod(int i)
               {
                  fail();
               }

               boolean staticMethod(Invocation invocation, Number i)
               {
                  assertEquals(1, invocation.getMinInvocations());
                  assertEquals(1, invocation.getMaxInvocations());
                  return i.intValue() > 0;
               }
            };
         }
      };

      assertTrue(Collaborator.staticMethod(1));
   }

   @Test
   public void delegateOverridingInvocationCountConstraints(final Collaborator mock)
   {
      new NonStrictExpectations() {{
         mock.finalMethod(); maxTimes = 1;
         result = new Delegate() {
            char finalMethod(Invocation invocation)
            {
               if (invocation.getInvocationIndex() == 0) {
                  assertEquals(0, invocation.getMinInvocations());
                  assertEquals(1, invocation.getMaxInvocations());
               }
               else {
                  assertEquals(2, invocation.getMinInvocations());
                  assertEquals(2, invocation.getMaxInvocations());
               }

               invocation.setMinInvocations(2);
               invocation.setMaxInvocations(2);
               return 'a';
            }
         };
      }};

      assertEquals('a', mock.finalMethod());
      assertEquals('a', mock.finalMethod());
   }

   @Test(expected = IllegalArgumentException.class)
   public void delegateClassWithNoMethodMatchingTheExpectationSignature(final Collaborator mock)
   {
      new Expectations() {{
         mock.privateMethod();
         result = new Delegate() {
            float someDelegate(Invocation invocation) { return 1.0F; }
            void someOtherMethod() {}
         };
      }};

      assertEquals(1.0, mock.privateMethod(), 0.0);
   }

   @Test
   public void delegateWithDifferentMethodName()
   {
      new NonStrictExpectations() {
         Collaborator mock;

         {
            mock.nativeMethod(anyBoolean);
            result = new Delegate() {
               long differentName(Invocation invocation, boolean b)
               {
                  assertEquals(1, invocation.getInvocationCount());
                  assertTrue(b);
                  return 3L;
               }
            };
         }
      };

      assertEquals(3L, new Collaborator().nativeMethod(true));
   }

   @Test
   public void consecutiveDelegatesForTheSameExpectation(final Collaborator mock)
   {
      new Expectations() {{
         mock.getValue();
         returns(
            new Delegate() {
               int delegate(Invocation invocation)
               {
                  assertSame(mock, invocation.getInvokedInstance());
                  assertEquals(1, invocation.getMinInvocations());
                  assertEquals(3, invocation.getMaxInvocations());
                  return invocation.getInvocationCount();
               }
            },
            new Delegate() {
               int delegate(Invocation invocation)
               {
                  return invocation.getInvocationCount();
               }
            },
            new Delegate() {
               int delegate(Invocation invocation)
               {
                  assertEquals(3, invocation.getInvocationCount());
                  throw new SecurityException();
               }
            });
      }};

      assertEquals(1, mock.getValue());
      assertEquals(2, mock.getValue());

      try {
         mock.getValue();
         fail();
      }
      catch (SecurityException e) {
         // OK
      }
   }

   // In forEachInvocation objects ////////////////////////////////////////////////////////////////////////////////////

   @Test
   public void invocationValidatorsForTwoSeparateExpectations(final Collaborator mock)
   {
      new Expectations() {{
         //noinspection unchecked
         mock.addElements((Collection<String>) any);
         forEachInvocation = new Object() {
            void delegate1(Invocation invocation, Collection<String> elements)
            {
               assertSame(mock, invocation.getInvokedInstance());
               assertEquals(1, invocation.getInvocationCount());
               assertNotNull(elements);
            }
         };

         mock.addElements(null);
         forEachInvocation = new Object() {
            void delegate2(Invocation invocation, Collection<String> elements)
            {
               assertSame(mock, invocation.getInvokedInstance());
               assertEquals(1, invocation.getInvocationCount());
               assertNull(elements);
            }
         };
      }};

      mock.addElements(Collections.<String>emptyList());
      mock.addElements(null);
   }

   @SuppressWarnings({"deprecation"})
   @Test
   public void useOfContextParametersForJREMethods() throws Exception
   {
      new NonStrictExpectations() {
         @Mocked({"runFinalizersOnExit", "exec"}) Runtime rt;

         {
            Runtime.runFinalizersOnExit(anyBoolean); minTimes = 1;
            result = new Delegate() {
               void delegate(Invocation inv, boolean b)
               {
                  assertNull(inv.getInvokedInstance());
                  assertEquals(1, inv.getInvocationCount());
                  assertEquals(1, inv.getMinInvocations());
                  assertEquals(-1, inv.getMaxInvocations());
                  assertTrue(b);
               }
            };

            rt.exec(anyString, null); maxTimes = 1;
            forEachInvocation = new Object() {
               void exec(Invocation inv, String command, String[] envp)
               {
                  assertSame(Runtime.getRuntime(), inv.getInvokedInstance());
                  assertEquals(0, inv.getInvocationIndex());
                  assertEquals(0, inv.getMinInvocations());
                  assertEquals(1, inv.getMaxInvocations());
                  assertNotNull(command);
                  assertNull(envp);
               }
            };
         }
      };

      Runtime.runFinalizersOnExit(true);
      assertNull(Runtime.getRuntime().exec("test", null));
   }
}
