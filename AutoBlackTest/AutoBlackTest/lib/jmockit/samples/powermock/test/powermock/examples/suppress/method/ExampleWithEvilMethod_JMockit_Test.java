/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.suppress.method;

import org.junit.*;

import static mockit.Mockit.*;
import static org.junit.Assert.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/suppress/method/ExampleWithEvilMethodTest.java">PowerMock version</a>
 */
public final class ExampleWithEvilMethod_JMockit_Test
{
   @Test
   public void testSuppressMethod()
   {
      stubOutClass(ExampleWithEvilMethod.class, "getEvilMessage");

      String message = "myMessage";
      ExampleWithEvilMethod tested = new ExampleWithEvilMethod(message);

      assertEquals(message + "null", tested.getMessage());
   }
}
