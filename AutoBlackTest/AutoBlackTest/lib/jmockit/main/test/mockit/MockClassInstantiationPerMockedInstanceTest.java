/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.io.*;
import java.net.*;
import java.util.*;

import org.junit.*;

import static mockit.Instantiation.*;
import static mockit.Mockit.*;
import static org.junit.Assert.*;

import mockit.internal.*;

@UsingMocksAndStubs(MockClassInstantiationPerMockedInstanceTest.MockClass1.class)
public final class MockClassInstantiationPerMockedInstanceTest
{
   static final class RealClass1
   {
      final int value;
      RealClass1(int value) { this.value = value; }
      static void doSomething() { throw new RuntimeException(); }
      int performComputation(int a, boolean b) { return b ? a : -a; }
   }

   static final class RealClass2
   {
      static void doSomething() { throw new RuntimeException(); }
      int performComputation(int a, boolean b) { return b ? a : -a; }
   }

   static final class RealClass3
   {
      static void doSomething() { throw new RuntimeException(); }
      int performComputation(int a, boolean b) { return b ? a : -a; }
   }

   static final class RealClass4
   {
      static void doSomething() { throw new RuntimeException(); }
      int performComputation(int a, boolean b) { return b ? a : -a; }
   }

   @MockClass(realClass = RealClass1.class, instantiation = PerMockedInstance)
   static final class MockClass1
   {
      static Object firstInstance;
      static Object secondInstance;
      int value;

      MockClass1()
      {
         if (firstInstance == null) {
            firstInstance = this;
         }
         else {
            assertNull(secondInstance);
            secondInstance = this;
         }
      }

      @Mock void $init(int value)
      {
         this.value = value;
      }

      @Mock void doSomething()
      {
         assertSame(firstInstance, this);
         assertNull(secondInstance);
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotNull(firstInstance);
         assertNotSame(firstInstance, this);
         assertSame(secondInstance, this);
         assertEquals(123, value);
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass2.class, instantiation = PerMockedInstance)
   static final class MockClass2
   {
      static Object firstInstance;
      static Object secondInstance;

      MockClass2()
      {
         if (firstInstance == null) {
            firstInstance = this;
         }
         else {
            assertNull(secondInstance);
            secondInstance = this;
         }
      }

      @Mock void doSomething()
      {
         assertSame(firstInstance, this);
         assertNull(secondInstance);
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotNull(firstInstance);
         assertNotSame(firstInstance, this);
         assertSame(secondInstance, this);
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass3.class, instantiation = PerMockedInstance)
   static final class MockClass3
   {
      static Object firstInstance;
      static Object secondInstance;

      MockClass3()
      {
         if (firstInstance == null) {
            firstInstance = this;
         }
         else {
            assertNull(secondInstance);
            secondInstance = this;
         }
      }

      @Mock void doSomething()
      {
         assertSame(firstInstance, this);
         assertNull(secondInstance);
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotNull(firstInstance);
         assertNotSame(firstInstance, this);
         assertSame(secondInstance, this);
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass4.class, instantiation = PerMockedInstance)
   static final class MockClass4
   {
      static Object firstInstance;
      static Object secondInstance;

      MockClass4()
      {
         if (firstInstance == null) {
            firstInstance = this;
         }
         else {
            assertNull(secondInstance);
            secondInstance = this;
         }
      }

      @Mock void doSomething()
      {
         assertSame(firstInstance, this);
         assertNull(secondInstance);
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotNull(firstInstance);
         assertNotSame(firstInstance, this);
         assertSame(secondInstance, this);
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @BeforeClass
   public static void setUpClassLevelMocks()
   {
      setUpMocks(MockClass2.class);
   }

   @Before
   public void setUpMethodLevelMocks()
   {
      setUpMock(RealClass3.class, MockClass3.class);
   }

   @Test
   public void mockInstancePerMockedInstanceInClassAndFixtureScopes()
   {
      assertMockClass1();
      assertMockClass2();
      assertMockClass3();
      assertEquals(1, new RealClass4().performComputation(1, true));
   }

   private void assertMockClass1()
   {
      MockClass1.firstInstance = null;
      MockClass1.secondInstance = null;
      RealClass1.doSomething();
      RealClass1 realClass = new RealClass1(123);
      assertEquals(2, realClass.performComputation(1, true));
      assertEquals(2, realClass.performComputation(3, true));
   }

   private void assertMockClass2()
   {
      MockClass2.firstInstance = null;
      MockClass2.secondInstance = null;
      RealClass2.doSomething();
      RealClass2 realClass = new RealClass2();
      assertEquals(2, realClass.performComputation(1, true));
      assertEquals(2, realClass.performComputation(3, true));
   }

   private void assertMockClass3()
   {
      MockClass3.firstInstance = null;
      MockClass3.secondInstance = null;
      RealClass3.doSomething();
      RealClass3 realClass = new RealClass3();
      assertEquals(2, realClass.performComputation(1, true));
      assertEquals(2, realClass.performComputation(3, true));
   }

   private void assertMockClass4()
   {
      RealClass4.doSomething();
      RealClass4 realClass = new RealClass4();
      assertEquals(2, realClass.performComputation(1, true));
      assertEquals(2, realClass.performComputation(3, true));
   }

   @Test
   public void mockInstancePerMockedInstanceInAllScopes()
   {
      setUpMocks(MockClass4.class);

      assertMockClass1();
      assertMockClass2();
      assertMockClass3();
      assertMockClass4();
   }

   @MockClass(realClass = URL.class, instantiation = Instantiation.PerMockedInstance)
   public static final class MockURL
   {
      public URL it;

      @Mock(reentrant = true)
      public InputStream openStream() throws IOException
      {
         if ("test".equals(it.getHost())) {
            return new ByteArrayInputStream("response".getBytes());
         }

         return it.openStream();
      }
   }

   @Test
   public void reentrantStartupMockForJREClass() throws Exception
   {
      setUpStartupMocks(MockURL.class);

      InputStream response = new URL("http://test").openStream();

      assertEquals("response", new Scanner(response).nextLine());

      new RedefinitionEngine().restoreToDefinitionBeforeStartup(URL.class);
   }
}