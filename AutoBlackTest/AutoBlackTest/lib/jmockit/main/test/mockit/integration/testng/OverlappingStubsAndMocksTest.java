/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.testng;

import org.testng.annotations.*;

import mockit.*;
import mockit.internal.*;

/**
 * Overlapping mocks/stubbing for the same real class is currently not supported by the Mockups API.
 */
@UsingMocksAndStubs(RealClass.class)
public final class OverlappingStubsAndMocksTest
{
   @MockClass(realClass = RealClass.class)
   static final class TheMockClass
   {
      @Mock(invocations = 1)
      void doSomething() {}
   }

   @Test
   public void firstTest()
   {
      Mockit.setUpMocks(TheMockClass.class);
      RealClass.doSomething();
   }

   @Test(dependsOnMethods = "firstTest", expectedExceptions = MissingInvocation.class)
   public void secondTest()
   {
      Mockit.setUpMocks(TheMockClass.class);
      RealClass.doSomething();
      // Fails with an unexpected final invocation count, caused by duplicate internal state for the
      // "doSomething" mock. The duplication, in turn, is caused by "RealClass" not being restored
      // after the first test, since it was stubbed out for the whole test class.
   }
}

final class RealClass
{
   static void doSomething() { throw new RuntimeException(); }
}
