/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package integrationTests;

import java.io.*;
import java.util.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static mockit.Mockit.*;
import static org.junit.Assert.*;
import org.junit.*;

import mockit.*;

@SuppressWarnings({"UnusedDeclaration", "ClassWithTooManyMethods"})
public final class CTest
{
   private String testData;

   @Test
   public void packageVoidNoArgs()
   {
      setUpMock(C.class, E.class);

      E.noReturnCalled = false;
      C c = new C();
      c.noReturn();
      assertTrue("noReturn() wasn't called", E.noReturnCalled);
   }

   public static class E
   {
      static boolean noReturnCalled;

      @Mock
      public int i()
      {
         System.out.println("E.i");
         return 2;
      }

      @Mock
      public void noReturn()
      {
         noReturnCalled = true;
      }
   }

   @Test
   public void protectedVoid1Arg()
   {
      F f = new F();
      setUpMock(C.class, f);

      C c = new C();
      c.setSomeValue("test");
      assertEquals("mock setSomeValue not called", "test", f.someValue);
   }

   public static class F
   {
      String someValue = "";

      @Mock
      public void setSomeValue(String someValue)
      {
         this.someValue = someValue;
      }
   }

   @Test
   public void primitiveLongAndIntParameters()
   {
      setUpMocks(PrimitiveLongAndIntParametersMock.class);
      PrimitiveLongAndIntParametersMock.sum = 0;
      C.validateValues(1L, 2);
      assertEquals(3L, PrimitiveLongAndIntParametersMock.sum);
   }

   @MockClass(realClass = C.class)
   public static class PrimitiveLongAndIntParametersMock
   {
      static long sum;

      @Mock
      public static void validateValues(long v1, int v2)
      {
         sum = v1 + v2;
      }
   }

   @Test
   public void primitiveNumericParameters()
   {
      setUpMock(C.class, new Object()
      {
         @Mock
         public double sumValues(byte v1, short v2, int v3, long v4, float v5, double v6)
         {
            return 0.0;
         }
      });

      double sum = new C().sumValues((byte) 1, (short) 2, 3, 4L, 5.0F, 6.0);
      assertEquals(0.0, sum, 0.0);
   }

   @Test
   public void defaultConstructor()
   {
      //noinspection InstantiationOfUtilityClass
      setUpMock(C.class, new DefaultConstructorMock());

      DefaultConstructorMock.x = 0;
      new C();

      assertEquals(1, DefaultConstructorMock.x);
   }

   public static class DefaultConstructorMock
   {
      static int x;

      @Mock
      public void $init() { x = 1; }
   }

   @Test
   public void constructor1Arg()
   {
      setUpMocks(F2.class);

      C c = new C("test");
      assertNull(c.getSomeValue());
   }

   @MockClass(realClass = C.class)
   public static class F2
   {
      @Mock
      public void $init(String someValue)
      {
         // do nothing
      }
   }

   @Test
   public void twoConstructorsWithMultipleArgs()
   {
      setUpMock(RealClassWithTwoConstructors.class, new MockConstructors());

      new RealClassWithTwoConstructors("a", 'b');
      assertEquals("ab", testData);

      new RealClassWithTwoConstructors("a", 'b', 1);
      assertEquals("ab1", testData);
   }

   public static class RealClassWithTwoConstructors
   {
      RealClassWithTwoConstructors(String a, char b) {}
      RealClassWithTwoConstructors(String a, char b, int c) {}
   }

   final class MockConstructors
   {
      @Mock void $init(String a, char b) { testData = a + b; }
      @Mock void $init(String a, char b, int c) { testData = a + b + c; }
   }

   @Test
   public void privateStaticVoidNoArgs()
   {
      //noinspection InstantiationOfUtilityClass
      setUpMocks(new H());

      C.printText();
      assertEquals("H.doPrintText", C.printedText);
   }

   @MockClass(realClass = C.class)
   public static class H
   {
      @Mock
      public static void doPrintText() { C.printedText = "H.doPrintText"; }
   }

   @Test
   public void staticVoid1ArgOverload()
   {
      setUpMock(C.class.getName(), H2.class);

      C.printedText = "";
      C.printText("mock");
      assertEquals("", C.printedText);
   }

   public static class H2
   {
      @Mock
      public static void printText(String text) { assertEquals("mock", text); }
   }

   @Test
   public void int1ArgWithWildcard()
   {
      setUpMock(C.class, new I());

      Collection<String> names = asList("abc", "G20", "xyz");
      int c = new C().count(names);
      assertEquals(0, c);
   }

   public static class I { @Mock public int count(Collection<?> items) { return 0; } }

   @Test
   public void genericList2Args()
   {
      setUpMock(C.class, J.class);

      Collection<String> names = asList("abc", "G20", "xyz");
      names = new C().orderBy(names, false);
      //noinspection AssertEqualsBetweenInconvertibleTypes
      assertEquals(emptyList(), names);
   }

   public static class J
   {
      @Mock
      public <E extends Comparable<E>> List<E> orderBy(Collection<E> items, boolean asc)
      {
         return emptyList();
      }
   }

   @Test
   public void throwsException() throws FileNotFoundException
   {
      setUpMock(C.class, K.class);

      new C().loadFile("temp");
      // no exception expected
      assertTrue(K.executed);
   }

   public static class K
   {
      static boolean executed;

      @Mock public void loadFile(String name) { executed = true; }
   }

   @Test
   public void throwsRuntimeException() throws FileNotFoundException
   {
      setUpMock(C.class, L.class);

      try {
         new C().loadFile("temp");
         fail();
      }
      catch (IllegalArgumentException ignore) {
         // passed
      }
   }

   public static class L
   {
      @Mock
      public void loadFile(String name) throws FileNotFoundException
      {
         throw new IllegalArgumentException();
      }
   }

   @Test(expected = TooManyListenersException.class)
   public void throwsCheckedExceptionNotInThrowsOfRealMethod() throws Exception
   {
      setUpMock(C.class, MockThrowingCheckedExceptionNotInRealMethodThrowsClause.class);

      new C().loadFile("test");
      fail();
   }

   public static class MockThrowingCheckedExceptionNotInRealMethodThrowsClause
   {
      @Mock
      public void loadFile(String name) throws TooManyListenersException
      {
         throw new TooManyListenersException();
      }
   }

   @Test
   public void varargs()
   {
      setUpMock(C.class, M.class);

      new C().printArgs(1, true, "test");
      assertEquals("mock", C.printedText);
   }

   public static class M
   {
      @Mock
      public void printArgs(Object... args)
      {
         C.printedText = "mock";
      }
   }

   @Test
   public void testRedefineOneClassTwoTimes()
   {
      setUpMock(C.class, D.class);
      boolean b = C.b();
      assertFalse(b);

      setUpMock(C.class, D.class);
      boolean b2 = C.b();
      assertFalse(b2);
   }

   public static final class D
   {
      @Mock
      public static boolean b()
      {
         return false;
      }
   }

   @Test
   public void redefineOneClassThreeTimes()
   {
      setUpMock(Ct.class, Cf.class);
      assertFalse(Ct.b());

      setUpMock(Ct.class, Ce.class);
      setUpMock(Ct.class, Ce.class);

      setUpMock(Ct.class, Cf.class);
      assertFalse(Ct.b());
   }

   public static class Ct { @Mock public static boolean b() { return true; } }
   public static class Cf { @Mock public static boolean b() { return false; } }
   public static class Ce { @Mock public static boolean b() { return true; }}

   @Test
   public void redefineMultipleClasses()
   {
      setUpMock(C.class, D.class);
      setUpMock(C2.class, N.class);

      boolean b = C.b();
      assertFalse(b);

      List<C2> c2Found = new C().findC2();
      assertNull(c2Found.get(0).getCode());
   }

   public static class N
   {
      @Mock
      public String getCode() { return null; }
   }

   @Test
   public void mockInstanceState()
   {
      O mock = new O();
      setUpMock(C.class, mock);

      C c = new C("some data");

      assertEquals(mock.value, c.getSomeValue());
   }

   public static class O
   {
      String value = "mock data";

      @Mock
      public String getSomeValue() { return value; }
   }

   @Test
   public void twoMockInstances()
   {
      P mock1 = new P(new C2(123, "one23"));
      setUpMock(C.class, mock1);

      C2Mock mock2 = new C2Mock();
      mock2.code = "mock2";
      setUpMock(C2.class, mock2);

      List<C2> c2Found = new C().findC2();
      assertEquals(mock1.data, c2Found);

      C2 c2 = c2Found.get(0);
      assertEquals(mock2.code, c2.getCode());
   }

   public static class P
   {
      List<C2> data;

      P(C2... data) { this.data = asList(data); }

      @Mock
      public List<C2> findC2() { return data; }
   }

   public class C2Mock
   {
      private String code;

      C2Mock() {}

      @Mock
      public String getCode() { return code; }
   }

   @Test
   public void redefineMethodsWithInnerMockClass()
   {
      try {
         setUpMock(C2.class, C2Mock.class);
         fail();
      }
      catch (IllegalArgumentException e) {
         // OK
      }
   }

   // Redefines the same as C2Mock, since inherited public methods ARE also considered.
   public class C3Mock extends C2Mock
   {
      C3Mock() {}
   }

   @Test
   public void redefineMethodsWithExtendedMockClass()
   {
      setUpMock(C2.class, new C3Mock());
      C2 c2 = new C2(12, "c2");
      assertNull(c2.getCode());
   }

   @Test
   public void mockInstanceOfAnonymousInnerClass()
   {
      setUpMock(C.class, new Object()
      {
         @Mock
         public String getSomeValue() { return "test data"; }
      });

      C c = new C("some data");

      assertEquals("test data", c.getSomeValue());
   }

   @Test
   public void mockInstanceOfAnonymousInnerClassUsingParentData()
   {
      //noinspection serial
      setUpMock(C.class, new Serializable()
      {
         @Mock
         public String getSomeValue() { return testData; }
      });

      testData = "my test data";
      C c = new C("some data");

      assertEquals(testData, c.getSomeValue());
   }

   @Test
   public void accessToInstanceUnderTest()
   {
      setUpMock(C.class, new Object()
      {
         // Instance under test. Warning: calling redefined methods may lead to infinite recursion!
         C it;

         @Mock @Override
         public String toString() { return it.getSomeValue().toUpperCase(); }
      });

      String value = new C("test data").toString();

      assertEquals("TEST DATA", value);
   }

   @Test
   public void attemptToMockNonExistentMethodInRealClass()
   {
      try {
         setUpMock(C.class, ClassWithMockMethodForNonExistentRealMethod.class);
         fail("Should have thrown " + IllegalArgumentException.class);
      }
      catch (IllegalArgumentException e) {
         // OK
      }
   }

   public static class ClassWithMockMethodForNonExistentRealMethod
   {
      @Mock
      public void nonExistent() {}
      
      int helperMethodNotIntendedAsMock(String s) { return s.length(); }
   }

   @Test
   public void mockJREClasses()
   {
      setUpMock(Date.class, new MockDate());
      assertEquals(0L, new Date().getTime());
      assertTrue(new Date().before(null));

      setUpMock(Date.class, MockDate.class);
      assertEquals(0L, new Date().getTime());
      assertTrue(new Date().before(null));

      setUpMock(String.class, MockString.class);
      assertEquals("0", String.valueOf(1.2));
      assertEquals("1", String.valueOf(5.0f));

      setUpMock(String.class, new MockString());
      assertEquals("0", String.valueOf(1.2));
      assertEquals("1", String.valueOf(5.0f));
   }

   public static class MockDate
   {
      @Mock public long getTime() { return 0L; }
      @Mock public static boolean before(Date d) { return true; }
   }

   public static class MockString
   {
      @Mock public static String valueOf(double d) { return "0"; }
      @Mock public String valueOf(float f) { return "1"; }
   }

   @Test
   public void staticMockMethodForNonStaticRealMethod()
   {
      setUpMock(CX.class, C4Mock.class);

      int i = new CX().i("");

      assertEquals(2, i);
   }

   public static class C4Mock
   {
      @Mock
      public static int i(String s) { return 2; }
   }

   @Test
   public void nonStaticMockMethodForStaticRealMethod()
   {
      setUpMock(CX.class, new CXMock());

      boolean b = CX.b("");

      assertFalse(b);
   }

   public static class CX
   {
      public int i(String s) { return 1; }

      public static boolean b(String s) { return true; }
   }

   public class CXMock
   {
      CXMock() {}

      @Mock
      public boolean b(String s) { return false; }
   }

   @Test
   public void mockMethodWithDifferentReturnType()
   {
      try {
         setUpMock(C.class, MockWithDifferentButAssignableReturnType.class);
         fail();
      }
      catch (IllegalArgumentException e) {
         // passed
      }
   }

   public static class MockWithDifferentButAssignableReturnType
   {
      @Mock
      public final java.sql.Date createOtherObject(boolean b, Date d, int a)
      {
         return new java.sql.Date(d.getTime());
      }
   }
}
