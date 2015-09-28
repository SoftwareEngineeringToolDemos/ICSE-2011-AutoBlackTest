/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import org.testng.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;

import mockit.*;

public class BaseTestNGDecoratorTest implements IHookable
{
   // Makes sure TestNG integration works with test classes which implement IHookable.
   public void run(IHookCallBack callBack, ITestResult testResult)
   {
      callBack.runTestMethod(testResult);
   }

   public static class RealClass1
   {
      public String getValue() { return "REAL1"; }
   }

   @MockClass(realClass = RealClass1.class)
   public static class MockClass1
   {
      @Mock
      public String getValue() { return "TEST1"; }
   }

   @BeforeMethod
   public final void beforeBase()
   {
      assertEquals("REAL1", new RealClass1().getValue());
      Mockit.setUpMocks(MockClass1.class);
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @AfterMethod
   public final void afterBase()
   {
      assertEquals("TEST1", new RealClass1().getValue());
   }
}
