/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import java.io.*;
import javax.security.auth.login.*;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import mockit.*;
import mockit.internal.*;

public final class TestNGDecoratorTest extends BaseTestNGDecoratorTest
{
   public static class RealClass2
   {
      public String getValue() { return "REAL2"; }
   }

   @MockClass(realClass = RealClass2.class)
   public static class MockClass2
   {
      @Mock
      public String getValue() { return "TEST2"; }
   }

   @Test
   public void setUpAndUseSomeMocks()
   {
      assertEquals("TEST1", new RealClass1().getValue());
      assertEquals("REAL2", new RealClass2().getValue());

      Mockit.setUpMocks(MockClass2.class);

      assertEquals("TEST2", new RealClass2().getValue());
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @Test
   public void setUpAndUseMocksAgain()
   {
      assertEquals("TEST1", new RealClass1().getValue());
      assertEquals("REAL2", new RealClass2().getValue());

      Mockit.setUpMocks(MockClass2.class);

      assertEquals("TEST2", new RealClass2().getValue());
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @AfterMethod
   public void afterTest()
   {
      assertEquals("REAL2", new RealClass2().getValue());
   }

   @SuppressWarnings("ClassMayBeInterface")
   public static class Temp {}
   private static final Temp temp = new Temp();

   @DataProvider(name = "data")
   public Object[][] createData1()
   {
      return new Object[][] { {temp} };
   }

   @Test(dataProvider = "data")
   public void checkNoMockingOfParametersWhenUsingDataProvider(Temp t)
   {
      assertSame(temp, t);
   }

   @Test
   public void checkMockingOfParameterWhenNotUsingDataProvider(Temp mock)
   {
      assertNotSame(temp, mock);
   }

   @Test(expectedExceptions = UnexpectedInvocation.class)
   public void mockMethodWithViolatedInvocationCountConstraint() throws Exception
   {
      new MockUp<LoginContext>() {
         @Mock(minInvocations = 1)
         void $init(String name) { assertEquals(name, "test"); }

         @Mock(invocations = 1)
         void login() {}
      };

      LoginContext context = new LoginContext("test");
      context.login();
      context.login();
   }

   @Test
   public void mockFile()
   {
      new Expectations() {
         final File directory;

         {
            directory = new File("dirName");
            directory.mkdir();
         }
      };

      new File("dirName").mkdir();
   }
}
