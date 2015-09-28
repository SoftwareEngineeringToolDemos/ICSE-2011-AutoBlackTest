/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4;

import org.junit.*;
import static org.junit.Assert.*;

import mockit.*;

public class BaseJUnit4DecoratorTest
{
   public static final class RealClass0
   {
      public String getValue() { return "REAL0"; }
   }

   @MockClass(realClass = RealClass0.class)
   public static final class MockClass0
   {
      @Mock
      public String getValue() { return "TEST0"; }
   }

   @BeforeClass
   public static void beforeClass()
   {
      assertEquals("REAL0", new RealClass0().getValue());
      Mockit.setUpMocks(MockClass0.class);
      assertEquals("TEST0", new RealClass0().getValue());
   }

   public static final class RealClass1
   {
      public String getValue() { return "REAL1"; }
   }

   @MockClass(realClass = RealClass1.class)
   public static final class MockClass1
   {
      @Mock
      public String getValue() { return "TEST1"; }
   }

   @Before
   public final void beforeBase()
   {
      assertEquals("REAL1", new RealClass1().getValue());
      Mockit.setUpMocks(MockClass1.class);
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @After
   public final void afterBase()
   {
      assertEquals("TEST0", new RealClass0().getValue());
      assertEquals("TEST1", new RealClass1().getValue());
   }

   @AfterClass
   public static void afterClass()
   {
      assertEquals("REAL0", new RealClass0().getValue());
      assertEquals("REAL1", new RealClass1().getValue());
   }
}
