/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.suppress.constructor;

import static org.junit.Assert.assertNull;

import org.junit.*;

import mockit.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/suppress/constructor/ExampleWithEvilConstructorTest.java">PowerMock version</a>
 */
public final class ExampleWithEvilConstructor_JMockit_Test
{
   @Test
   public void testSuppressOwnConstructor()
   {
      Mockit.stubOutClass(ExampleWithEvilConstructor.class, "(String)");

      ExampleWithEvilConstructor tested = new ExampleWithEvilConstructor("test");

      assertNull(tested.getMessage());
   }

   @Test(expected = UnsatisfiedLinkError.class)
   public void testNotSuppressOwnConstructor()
   {
      String message = "myMessage";
      new ExampleWithEvilConstructor(message);
   }
}
