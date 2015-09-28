/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import org.junit.*;

import static mockit.Instantiation.*;
import static mockit.Mockit.*;
import static org.junit.Assert.*;

@UsingMocksAndStubs(MockClassInstantiationPerInvocationTest.MockClass1.class)
public final class MockClassInstantiationPerInvocationTest
{
   static final class RealClass1
   {
      static void doSomething()
      {
         throw new RuntimeException();
      }

      int performComputation(int a, boolean b)
      {
         return b ? a : -a;
      }
   }

   static final class RealClass2
   {
      static void doSomething()
      {
         throw new RuntimeException();
      }

      int performComputation(int a, boolean b)
      {
         return b ? a : -a;
      }
   }

   static final class RealClass3
   {
      static void doSomething()
      {
         throw new RuntimeException();
      }

      int performComputation(int a, boolean b)
      {
         return b ? a : -a;
      }
   }

   static final class RealClass4
   {
      static void doSomething()
      {
         throw new RuntimeException();
      }

      int performComputation(int a, boolean b)
      {
         return b ? a : -a;
      }
   }

   @MockClass(realClass = RealClass1.class, instantiation = PerMockInvocation)
   static final class MockClass1
   {
      static Object previousInstance;

      MockClass1()
      {
         assertNotSame(previousInstance, this);
      }

      @Mock void doSomething()
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass2.class)
   static final class MockClass2
   {
      static Object previousInstance;

      MockClass2()
      {
         assertNotSame(previousInstance, this);
      }

      @Mock void doSomething()
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass3.class, instantiation = PerMockInvocation)
   static final class MockClass3
   {
      static Object previousInstance;

      MockClass3()
      {
         assertNotSame(previousInstance, this);
      }

      @Mock void doSomething()
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @MockClass(realClass = RealClass4.class)
   static final class MockClass4
   {
      static Object previousInstance;

      MockClass4()
      {
         assertNotSame(previousInstance, this);
      }

      @Mock void doSomething()
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
      }

      @Mock int performComputation(int a, boolean b)
      {
         assertNotSame(previousInstance, this);
         previousInstance = this;
         assertTrue(a > 0);
         assertTrue(b);
         return 2;
      }
   }

   @BeforeClass
   public static void setUpClassLevelMocks()
   {
      setUpMocksAndStubs(MockClass2.class);
   }

   @Before
   public void setUpMethodLevelMocks()
   {
      setUpMock(RealClass3.class, MockClass3.class);
   }

   @Test
   public void mockInstancePerInvocationInClassAndFixtureScopes()
   {
      assertMockClass1();
      assertMockClass2();
      assertMockClass3();
      assertEquals(1, new RealClass4().performComputation(1, true));
   }

   private void assertMockClass1()
   {
      RealClass1.doSomething();
      assertEquals(2, new RealClass1().performComputation(1, true));
   }

   private void assertMockClass2()
   {
      RealClass2.doSomething();
      assertEquals(2, new RealClass2().performComputation(1, true));
   }

   private void assertMockClass3()
   {
      RealClass3.doSomething();
      assertEquals(2, new RealClass3().performComputation(1, true));
   }

   private void assertMockClass4()
   {
      RealClass4.doSomething();
      assertEquals(2, new RealClass4().performComputation(1, true));
   }

   @Test
   public void mockInstancePerInvocationInAllScopes()
   {
      setUpMocks(MockClass4.class);

      assertMockClass1();
      assertMockClass2();
      assertMockClass3();
      assertMockClass4();
   }
}