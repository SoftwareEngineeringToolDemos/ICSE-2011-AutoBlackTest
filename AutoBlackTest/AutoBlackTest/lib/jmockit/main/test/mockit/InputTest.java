/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.*;

import static java.util.Arrays.*;
import org.junit.*;
import static org.junit.Assert.*;

public final class InputTest
{
   @SuppressWarnings("UnusedParameters")
   static class Collaborator
   {
      int getInt() { return 1; }
      short getShort() { return 56; }
      int getAnotherInt(String s) { return s.hashCode(); }
      String getString() { return ""; }
      private String getAnotherString() { return ""; }
      List<Integer> getListOfIntegers() { return null; }
      List<Boolean> getListOfBooleans() { return null; }
      static Map<String, List<Long>> getMapFromStringToListOfLongs() { return null; }
      Socket getSocket() { return null; }
      void throwSocketException() throws SocketException, IllegalAccessException {}
      <E extends CharSequence> List<E> genericMethod1() { return null; }
      <E extends Number> List<E> genericMethod2(E num) { return null; }
      <Value> Map<String, Value> genericMethod3() { return null; }
      <Key extends Number, Value> Map<Key, Value> genericMethod4(Key key, Value value) { return null; }
      Collaborator parent() { return null; }
      ClassLackingNoArgsConstructor someMethod() { return new ClassLackingNoArgsConstructor(123); }
      ClassWhoseConstructorFails willAlwaysFail() { return new ClassWhoseConstructorFails(); }
   }

   static final class ClassLackingNoArgsConstructor
   {
      ClassLackingNoArgsConstructor(int i) { assert i >= 0; }
   }

   public static final class ClassWhoseConstructorFails
   {
      public ClassWhoseConstructorFails() { throw new UnsupportedOperationException(); }
   }

   @Mocked Collaborator mock;

   @Test
   public void specifyDefaultReturnValues()
   {
      new NonStrictExpectations() {
         @Input final int someIntValue = 123;
         @Input String uniqueId = "Abc5"; // fields not required to be final
         @Input final List<Integer> userIds = asList(4, 56, 278);
         @Input final List<Boolean> flags = asList(true, false, true);
         @Input final Map<String, List<Long>> complexData = new HashMap<String, List<Long>>() {{
            put("empty", new ArrayList<Long>());
            put("one", asList(1L));
            put("two", asList(10L, 20L));
         }};
         @Input Socket aSocket; // created with public no-args constructor and automatically assigned

         {
            mock.getAnotherInt("c"); result = 45;
            mock.getAnotherString(); result = null;
         }
      };

      assertEquals(123, mock.getInt());
      assertEquals(0, mock.getShort());
      assertEquals(123, mock.getAnotherInt("a"));
      assertEquals(45, mock.getAnotherInt("c"));
      assertEquals("Abc5", mock.getString());
      assertNull(mock.getAnotherString());
      assertEquals(asList(4, 56, 278), mock.getListOfIntegers());
      assertEquals(asList(true, false, true), mock.getListOfBooleans());
      assertNotNull(mock.getSocket());

      Map<String, List<Long>> data = Collaborator.getMapFromStringToListOfLongs();
      assertTrue(data.get("empty").isEmpty());
      assertTrue(data.get("one").contains(1L));
      assertEquals(2, data.get("two").size());
      assertEquals(10L, (long) data.get("two").get(0));
      assertEquals(20L, (long) data.get("two").get(1));

      assertEquals("Abc5", mock.getString());
      assertEquals(123, mock.getInt());
      assertEquals(123, mock.getAnotherInt("b"));
      assertNull(mock.getAnotherString());
   }

   @Test
   public void specifyUniqueReturnValueForMethodsWithGenericReturnTypes()
   {
      final List<String> list1 = asList("a", "b");
      final List<Integer> list2 = asList(1, 2);
      final Map<String, String> map1  = new HashMap<String, String>() {{ put("map", "map1"); }};
      final Map<Integer, String> map2 = new HashMap<Integer, String>() {{ put(123, "map2"); }};

      new Expectations() {
         @Input final List<String> names = list1;
         @Input final List<Integer> numbers = list2;
         @Input Map<String, String> defaultValues3 = map1;
         @Input Map<? extends Number, String> defaultValues4 = map2;
      };

      assertSame(list1, mock.genericMethod1());
      assertSame(list2, mock.genericMethod2(123));
      assertSame(map1, mock.genericMethod3());
      assertSame(map2, mock.genericMethod4(5L, "test"));
   }

   @Test(expected = SocketException.class)
   public void specifyDefaultExceptionToThrow() throws Exception
   {
      new Expectations() {
         @Input SocketException networkFailure;
      };

      mock.throwSocketException();
   }

   @Test
   public void instantiateClassWhoseNoArgsConstructorIsNotPublic()
   {
      new Expectations() {
         @Input Collaborator parent;
      };

      assertNotNull(mock.parent());
   }

   @Test(expected = InstantiationException.class)
   public void attemptToInstantiateClassLackingANoArgsConstructor() throws Throwable
   {
      new Expectations() {
         @Input ClassLackingNoArgsConstructor fail;
      };

      try {
         mock.someMethod();
         fail();
      }
      catch (RuntimeException e) {
         throw e.getCause();
      }
   }

   @Test(expected = UnsupportedOperationException.class)
   public void attemptToInstantiateClassWhoseConstructorFails()
   {
      new Expectations() {
         @Input ClassWhoseConstructorFails fail;
      };

      mock.willAlwaysFail();
   }

   @Test
   public void multipleInputFieldsOfTheSameReturnType()
   {
      new Expectations() {
         @Input int first = 1;
         @Input int second = 2;
      };

      assertEquals(1, mock.getInt());
      assertEquals(2, mock.getAnotherInt("2"));
      assertEquals(2, mock.getInt());
   }

   @Test
   public void multipleInputFieldsOfTheSameReturnTypeWithFixedNumberOfInvocationsOnTheFirst()
   {
      new Expectations() {
         @Input(invocations = 2) String first = "Abc";
         @Input String second = "Xyz";
      };

      assertEquals("Abc", mock.getString());
      assertEquals("Abc", mock.getAnotherString());
      assertEquals("Xyz", mock.getAnotherString());
      assertEquals("Xyz", mock.getString());
      assertEquals("Xyz", mock.getString());
   }

   @Test
   public void multipleInputFieldsOfTheSameReturnTypeWithFixedNumbersOfInvocationsOnBoth()
   {
      new Expectations() {
         @Input(invocations = 2) String first = "Abc";
         @Input(invocations = 3) String second = "Xyz";
      };

      assertEquals("Abc", mock.getString());
      assertEquals("Abc", mock.getAnotherString());
      assertEquals("Xyz", mock.getAnotherString());
      assertEquals("Xyz", mock.getString());
      assertEquals("Xyz", mock.getString());
      assertNull(mock.getAnotherString());
      assertNull(mock.getString());
   }

   @Test
   public void multipleInputFieldsOfTheSameCheckedExceptionType() throws IllegalAccessException
   {
      new Expectations() {
         @Input SocketException first;
         @Input final SocketException second = new SocketException("second one");
      };

      try {
         mock.throwSocketException();
         fail();
      }
      catch (SocketException e) {
         assertNull(e.getMessage());
      }

      for (int i = 1; i < 3; i++) {
         try {
            mock.throwSocketException();
            fail();
         }
         catch (SocketException e) {
            assertEquals("second one", e.getMessage());
         }
      }
   }

   public static final class DependencyAbc
   {
      int intReturningMethod() { return -1; }

      @SuppressWarnings("RedundantThrowsDeclaration")
      String stringReturningMethod() throws SomeCheckedException { return ""; }
   }

   public static final class SomeCheckedException extends Exception {}

   public static class TestedUnit
   {
      private final DependencyAbc abc = new DependencyAbc();

      public void doSomething()
      {
         int n = abc.intReturningMethod();

         for (int i = 0; i < n; i++) {
            String s;

            try {
               s = abc.stringReturningMethod();
            }
            catch (SomeCheckedException e) {
               // somehow handle the exception
               s = "Abc";
            }

            // do some other stuff
            s.toCharArray();
         }
      }
   }

   @Test
   public void doSomethingHandlesSomeCheckedException()
   {
      new Expectations() {
         DependencyAbc abc;

         @Input int iterations = 3;
         @Input SomeCheckedException onFirstIteration;
      };

      new TestedUnit().doSomething();
   }

   @Test
   public void specifyDefaultReturnByCombiningInputFieldsWithDynamicPartialMocking()
   {
      final Calendar fixedCal = new GregorianCalendar(2010, 4, 15);

      new NonStrictExpectations(Calendar.class) {
         @Input Calendar defaultCalendar = fixedCal;
      };

      assertSame(fixedCal, Calendar.getInstance());
      assertSame(fixedCal, Calendar.getInstance(TimeZone.getDefault()));
      assertSame(fixedCal, Calendar.getInstance(TimeZone.getTimeZone("CST")));
      assertSame(fixedCal, Calendar.getInstance(Locale.FRANCE));
      assertSame(fixedCal, Calendar.getInstance(TimeZone.getTimeZone("PST"), Locale.US));
   }

   @Test(expected = FileNotFoundException.class)
   public void specifyDefaultExceptionByCombiningInputFieldsWithDynamicPartialMocking() throws Exception
   {
      new NonStrictExpectations(File.class) {
         @Input IOException defaultCalendar = new FileNotFoundException();
      };

      File.createTempFile("abc", "");
   }

   public static class GenericClass<T>
   {
      T doSomething() { return null; }
      T doSomethingElse() { return null; }
      Collaborator getCollaborator() { return null; }
   }

   @Test
   public void specifyDefaultValueForMethodsReturningTypeParameterOfGenericClass()
   {
      final DependencyAbc d = new DependencyAbc();

      new Expectations() {
         final GenericClass<?> unused = null;
         @Input DependencyAbc abc = d;
      };

      GenericClass<DependencyAbc> gc1 = new GenericClass<DependencyAbc>();
      assertSame(d, gc1.doSomething());
      assertSame(d, gc1.doSomethingElse());
      assertNull(gc1.getCollaborator());

      GenericClass<Collaborator> gc2 = new GenericClass<Collaborator>();
      assertSame(d, gc2.doSomething());
   }

   @Test
   public void specifyDefaultValuesForMethodsReturningTypeParameterOfGenericClassInstantiations(GenericClass<?> mock)
   {
      new Expectations() {
         @Input String name = "test";
         @Input Double value = 4.5;
         @Input Collaborator collaborator;
      };

      Object v1 = mock.doSomething();
      assertEquals("test", v1); // gets the first default result, when more than one matches
      assertEquals("test", mock.doSomethingElse());
      assertNotNull(mock.getCollaborator());

      // Coincides with the type of the first default result, so it "works".
      GenericClass<String> gc1 = new GenericClass<String>();
      assertSame("test", gc1.doSomething());
      assertSame("test", gc1.doSomethingElse());
      assertNotNull(gc1.getCollaborator());

      // Same as "GenericClass<?>", so it doesn't work: generic methods return the first default value, of type String.
      GenericClass<Double> gc2 = new GenericClass<Double>();
      Object s = gc2.doSomething();
      assertEquals("test", s);
   }
}
