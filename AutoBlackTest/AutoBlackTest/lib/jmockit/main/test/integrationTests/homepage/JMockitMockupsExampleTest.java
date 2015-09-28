/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package integrationTests.homepage;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;

public final class JMockitMockupsExampleTest
{
   @Test
   public void testDoOperationAbc()
   {
      Mockit.setUpMocks(MockDependencyXyz.class);

      // In ServiceAbc#doOperationAbc(String s): "new DependencyXyz().doSomething(s);"
      Object result = new ServiceAbc().doOperationAbc("test");

      assertNotNull(result);
   }

   @MockClass(realClass = DependencyXyz.class)
   public static class MockDependencyXyz
   {
      @Mock(invocations = 1)
      public int doSomething(String value)
      {
         assertEquals("test", value);
         return 123;
      }
   }

   @Test // same as the previous test, but using an "in-line" (anonymous) mock class
   public void testDoOperationAbc_inlineVersion()
   {
      new MockUp<DependencyXyz>()
      {
         @Mock(invocations = 1)
         int doSomething(String value)
         {
            assertEquals("test", value);
            return 123;
         }
      };

      Object result = new ServiceAbc().doOperationAbc("test");

      assertNotNull(result);
   }
}
