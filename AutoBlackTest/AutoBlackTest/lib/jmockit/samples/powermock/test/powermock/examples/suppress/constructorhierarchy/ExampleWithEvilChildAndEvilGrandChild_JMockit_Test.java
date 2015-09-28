/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.suppress.constructorhierarchy;

import org.junit.*;

import static mockit.Mockit.*;
import static org.junit.Assert.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/suppress/constructorhierarchy/ExampleWithEvilParentAndEvilGrandParentTest.java">PowerMock version</a>
 */
public final class ExampleWithEvilChildAndEvilGrandChild_JMockit_Test
{
   @Test
   public void testSuppressConstructorHierarchy()
   {
      stubOut(EvilChild.class, EvilGrandChild.class);

      String message = "myMessage";
      ExampleWithEvilChildAndEvilGrandChild tested =
         new ExampleWithEvilChildAndEvilGrandChild(message);

      assertEquals(message, tested.getMessage());
   }

   @Test
   public void testSuppressConstructorOfEvilChild()
   {
      stubOut(EvilChild.class, EvilGrandChild.class);

      String message = "myMessage";
      new ExampleWithEvilChildAndEvilGrandChild(message);
   }

   @Test(expected = UnsatisfiedLinkError.class)
   public void testNotSuppressConstructorOfEvilChild()
   {
      String message = "myMessage";
      new ExampleWithEvilChildAndEvilGrandChild(message);
   }
}
