/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.bypassencapsulation;

import java.util.*;

import org.junit.*;

import static mockit.Deencapsulation.*;
import static org.junit.Assert.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/bypassencapsulation/ServiceHolderTest.java">PowerMock version</a>
 */
public final class ServiceHolder_JMockit_Test
{
   @Test
   public void testAddService()
   {
      ServiceHolder tested = new ServiceHolder();
      Object service = new Object();

      tested.addService(service);

      Set<String> services = getField(tested, "services");

      assertEquals("Size of the \"services\" Set should be 1", 1, services.size());
      assertSame(
         "The services Set should didn't contain the expect service",
         service, services.iterator().next());
   }

   @Test
   public void testRemoveService()
   {
      ServiceHolder tested = new ServiceHolder();
      Object service = new Object();

      // Get the hash set.
      Set<Object> servicesSet = getField(tested, Set.class);
      servicesSet.add(service);

      tested.removeService(service);

      assertTrue("Set should be empty after removeal.", servicesSet.isEmpty());
   }
}
