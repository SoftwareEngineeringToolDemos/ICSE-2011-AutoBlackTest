/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public final class ExpectationsUsingReflectionTest
{
   public interface BusinessInterface
   {
      void doOperation();
   }

   static class BaseCollaborator
   {
      @SuppressWarnings({"UnusedParameters"})
      void setValue(short value) {}
   }

   @SuppressWarnings({"UnusedDeclaration", "MethodOverloadsMethodOfSuperclass"})
   static final class Collaborator extends BaseCollaborator
   {
      static String xyz;

      private int value;
      private Integer value2;
      private String value3;

      Collaborator() {}
      Collaborator(Object value) { value3 = String.valueOf(value); }
      Collaborator(CharSequence value) { value3 = value.toString(); }
      Collaborator(int value) { this.value = value; }
      Collaborator(int value, Integer value2, String value3)
      {
         this.value = value; this.value2 = value2; this.value3 = value3;
      }

      private static String doInternal() { return "123"; }

      static void staticMethod(int i, Object o, Character c) {}
      static void staticMethod(int i, String s, char c) {}

      void setValue(int value) { this.value = value; }
      void setValue(long value) { this.value = (int) value; }
      void setValue(byte value) { this.value = value; }
      void setValue(char value) { this.value = value; }
      void setValue(float value) { this.value = (int) value; }
      void setValue(double value) { this.value = (int) value; }
      void setValue(boolean value) { this.value = value ? 1 : -1; }
      void setValue(String value) { value3 = value; }
      void setValue(Object value) { value3 = String.valueOf(value); }

      void doBusinessOperation(BusinessInterface operation) { operation.doOperation(); }

      private final class Inner
      {
         private final String s;

         Inner() { s = null; }
         Inner(String s, boolean b) { this.s = s; }
      }
   }

   @SuppressWarnings({"UnusedParameters"})
   static final class Collaborator2
   {
      static void staticMethod(int i, String s, char c) {}
      static void staticMethod(int i, Object o, Character c) {}
   }

   // Just to have a mock field so no empty expectation blocks exist.
   @Mocked Runnable unused;

   @Test
   public void expectInstanceMethodInvocation(final Collaborator mock)
   {
      new Expectations() {{ invoke(mock, "setValue", 2); }};

      mock.setValue(2);
   }

   @Test
   public void expectInstanceMethodInvocationsWithAnyArguments(final Collaborator mock)
   {
      new Expectations() {{
         invoke(mock, "setValue", anyInt);
         invoke(mock, "setValue", withAny(1));
         invoke(mock, "setValue", anyByte);
         invoke(mock, "setValue", anyShort);
         invoke(mock, "setValue", anyLong);
         invoke(mock, "setValue", anyChar);
         invoke(mock, "setValue", anyBoolean);
         invoke(mock, "setValue", anyFloat);
         invoke(mock, "setValue", anyDouble);
         invoke(mock, "setValue", anyString);
         invoke(mock, "setValue", withAny(""));
         invoke(mock, "setValue", withAny(Object.class));
         invoke(mock, "doBusinessOperation", withAny(BusinessInterface.class));
      }};

      mock.setValue(2);
      mock.setValue(-3);
      mock.setValue((byte) 45);
      mock.setValue((short) 23000);
      mock.setValue(987654321L);
      mock.setValue('S');
      mock.setValue(true);
      mock.setValue(6.7F);
      mock.setValue(6.7);
      mock.setValue("test");
      mock.setValue(null);
      mock.setValue(new Object());
      mock.doBusinessOperation(null);
   }

   @Test
   public void expectStaticMethodInvocations()
   {
      new Expectations() {
         final Collaborator mock = null;

         {
            invoke(Collaborator.class, "doInternal"); result = "test";
            invoke(Collaborator.class, "staticMethod", anyInt, withAny(Object.class), anyChar);
            invoke(Collaborator.class, "staticMethod", 2, "ab", 'd');
         }
      };

      assertEquals("test", Collaborator.doInternal());
      Collaborator.staticMethod(1, true, 'c');
      Collaborator.staticMethod(2, "ab", 'd');
   }

   @Test // the order of the results from "Class#getDeclaredMethods()" is different in JDK 7
   public void expectStaticMethodInvocationsWithMethodsInReverseOrderInTheMockedClass()
   {
      new Expectations() {
         final Collaborator2 mock = null;

         {
            invoke(Collaborator2.class, "staticMethod", anyInt, withAny(Object.class), anyChar);
            invoke(Collaborator2.class, "staticMethod", 2, "ab", 'd');
         }
      };

      Collaborator2.staticMethod(1, true, 'c');
      Collaborator2.staticMethod(2, "ab", 'd');
   }

   @Test
   public void expectMethodInvocationWithProxyArgument(final Collaborator mock, final BusinessInterface proxyArg)
   {
      new Expectations()
      {
         {
            invoke(mock, "doBusinessOperation", proxyArg);
         }
      };

      mock.doBusinessOperation(proxyArg);
   }

   @Test(expected = IllegalArgumentException.class)
   public void recordMethodInvocationWithNullArgument()
   {
      new NonStrictExpectations() {
         Collaborator mock;

         {
            invoke(mock, "setValue", any);
         }
      };
   }

   @Test
   public void setInstanceFieldByName()
   {
      final Collaborator collaborator = new Collaborator();

      new Expectations()
      {
         {
            setField(collaborator, "value", 123);
         }
      };

      assertEquals(123, collaborator.value);
   }

   @Test
   public void setInstanceFieldBy()
   {
      final Collaborator collaborator = new Collaborator();
      collaborator.value3 = "";

      new Expectations()
      {
         {
            setField(collaborator, "test");
         }
      };

      assertEquals("test", collaborator.value3);
   }

   @Test(expected = IllegalArgumentException.class)
   public void setInstanceFieldByTypeWhenNoCompatibleFieldExists()
   {
      final Collaborator collaborator = new Collaborator();

      new Expectations()
      {
         {
            setField(collaborator, 'X');
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void setInstanceFieldByTypeWhenMultipleCompatibleFieldsExist()
   {
      final Collaborator collaborator = new Collaborator();

      new Expectations()
      {
         {
            setField(collaborator, 56);
         }
      };
   }

   @Test
   public void setStaticFieldByName()
   {
      new Expectations()
      {
         {
            setField(Collaborator.class, "xyz", "test");
         }
      };

      assertEquals("test", Collaborator.xyz);
   }

   @Test
   public void setStaticFieldByType()
   {
      new Expectations()
      {
         {
            setField(Collaborator.class, "static");
         }
      };

      assertEquals("static", Collaborator.xyz);
   }

   @Test(expected = IllegalArgumentException.class)
   public void setStaticFieldByTypeWhenNoCompatibleFieldExists()
   {
      new Expectations()
      {
         {
            setField(Collaborator.class, 123);
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void setStaticFieldByTypeWhenMultipleCompatibleFieldsExist()
   {
      new Expectations()
      {
         {
            setField(Collaborator.class, Collections.<Object>emptyList());
         }
      };
   }

   @Test
   public void getInstanceFieldByName(final Collaborator mock)
   {
      mock.value = 123;

      new Expectations()
      {
         {
            assertEquals(123, getField(mock, "value"));
         }
      };
   }

   @Test
   public void getInstanceFieldByType(final Collaborator mock)
   {
      mock.value3 = "test";

      new Expectations()
      {
         {
            assertEquals("test", getField(mock, String.class));
         }
      };
   }


   @Test(expected = IllegalArgumentException.class)
   public void getInstanceFieldByTypeWhenNoCompatibleFieldExists(final Collaborator mock)
   {
      new Expectations()
      {
         {
            getField(mock, char.class);
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void getInstanceFieldByTypeWhenMultipleCompatibleFieldsExist(final Collaborator mock)
   {
      new Expectations()
      {
         {
            getField(mock, int.class);
         }
      };
   }

   @Test
   public void getStaticFieldByName()
   {
      Collaborator.xyz = "test";

      new Expectations()
      {
         {
            assertEquals("test", getField(Collaborator.class, "xyz"));
         }
      };
   }

   @Test
   public void getStaticFieldByType()
   {
      Collaborator.xyz = "static";

      new Expectations()
      {
         {
            assertEquals("static", getField(Collaborator.class, String.class));
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void getStaticFieldByTypeWhenNoCompatibleFieldExists()
   {
      new Expectations()
      {
         {
            getField(Collaborator.class, Integer.class);
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void getStaticFieldByTypeWhenMultipleCompatibleFieldsExist()
   {
      new Expectations()
      {
         {
            getField(Collaborator.class, Collection.class);
         }
      };
   }

   @Test
   public void createNewInstanceWithExplicitParameterTypes()
   {
      new Expectations()
      {
         private final String className = Collaborator.class.getName();

         {
            Class<?>[] paramTypes = {int.class};
            Collaborator c1 = newInstance(className, paramTypes, 1);
            assertEquals(1, c1.value);

            Class<?>[] paramTypes2 = {int.class, Integer.class, String.class};
            Collaborator c2 = newInstance(className, paramTypes2, 1, 2, "3");
            assertEquals(1, c2.value);
            assertSame(2, c2.value2);
            assertEquals("3", c2.value3);

            Collaborator c3 = newInstance(className, paramTypes2, 0, null, null);
            assertEquals(0, c3.value);
            assertNull(c3.value2);
            assertNull(c3.value3);
         }
      };
   }

   @Test
   public void createNewInstanceWithNonNullArguments()
   {
      new Expectations()
      {
         private final String className = Collaborator.class.getName();

         {
            Collaborator cString = newInstance(className, "test");
            assertEquals("test", cString.value3);

            Collaborator cObject = newInstance(className, new Object());
            assertTrue(cObject.value3.startsWith("java.lang.Object@"));

            Collaborator cInt = newInstance(className, 1);
            assertEquals(1, cInt.value);

            Collaborator c2 = newInstance(className, 1, 2, "3");
            assertEquals(1, c2.value);
            assertSame(2, c2.value2);
            assertEquals("3", c2.value3);

            Collaborator c3 = newInstance(className, 0, Integer.class, String.class);
            assertEquals(0, c3.value);
            assertNull(c3.value2);
            assertNull(c3.value3);
         }
      };
   }

   @Test(expected = IllegalArgumentException.class)
   public void createNewInstanceWithNonNullArgumentsButActuallyPassingNulls()
   {
      new Expectations()
      {
         {
            newInstance(Collaborator.class.getName(), 0, null, null);
         }
      };
   }

   @Test
   public void createNewInnerInstance()
   {
      final Collaborator collaborator = new Collaborator();

      new Expectations()
      {
         private final String className = Collaborator.Inner.class.getSimpleName();

         {
            Collaborator.Inner c1 = newInnerInstance(className, collaborator);
            assertNull(c1.s);

            Collaborator.Inner c2 = newInnerInstance(className, collaborator, "test", true);
            assertEquals("test", c2.s);
         }
      };
   }
}
