/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.suppress.staticinitializer;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;
import powermock.examples.suppress.staticinitializer.ExampleWithEvilStaticInitializer_JMockit_Test.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/suppress/staticinitializer/ExampleWithEvilStaticInitializerTest.java">PowerMock version</a>
 */
@UsingMocksAndStubs(MockExampleWithStaticInitializer.class)
public final class ExampleWithEvilStaticInitializer_JMockit_Test
{
   @MockClass(realClass = ExampleWithEvilStaticInitializer.class, stubs = "<clinit>")
   static class MockExampleWithStaticInitializer {}

   @Test
   public void testSuppressStaticInitializer()
   {
      String message = "myMessage";
      ExampleWithEvilStaticInitializer tested = new ExampleWithEvilStaticInitializer(message);
      assertEquals(message, tested.getMessage());
   }
}
