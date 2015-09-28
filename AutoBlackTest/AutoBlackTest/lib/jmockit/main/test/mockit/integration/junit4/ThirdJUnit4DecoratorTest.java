/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;

@UsingMocksAndStubs(ThirdJUnit4DecoratorTest.MockClass4.class)
public final class ThirdJUnit4DecoratorTest extends BaseJUnit4DecoratorTest
{
   public static final class RealClass4
   {
      public String getValue() { return "REAL4"; }
   }

   @MockClass(realClass = RealClass4.class)
   public static final class MockClass4
   {
      @Mock
      public String getValue() { return "TEST4"; }
   }

   @Test
   public void realClassesMockedInBaseClassMustStillBeMockedHere()
   {
      assertEquals("TEST0", new RealClass0().getValue());
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @Test
   public void realClassesMockedInOtherTestClassesMustNotBeMockedHere()
   {
      assertEquals("REAL2", new JUnit4DecoratorTest.RealClass2().getValue());
      assertEquals("REAL3", new SecondJUnit4DecoratorTest.RealClass3().getValue());
   }

   @Test
   public void useClassScopedMockDefinedForThisClass()
   {
      assertEquals("TEST4", new RealClass4().getValue());
   }
}