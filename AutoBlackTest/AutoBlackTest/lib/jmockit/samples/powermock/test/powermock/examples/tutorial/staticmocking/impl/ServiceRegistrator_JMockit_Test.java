/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.tutorial.staticmocking.impl;

import java.util.*;

import org.junit.*;

import mockit.*;

import static mockit.Deencapsulation.*;
import static org.junit.Assert.*;
import powermock.examples.tutorial.staticmocking.osgi.*;

/**
 * Unit tests using the JMockit API for the
 * {@link powermock.examples.tutorial.staticmocking.impl.ServiceRegistrator} class.
 * <p/>
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/tutorial/src/solution/java/demo/org/powermock/examples/tutorial/staticmocking/impl/ServiceRegistratorTest.java">PowerMock version</a>
 */
public final class ServiceRegistrator_JMockit_Test
{
   private ServiceRegistrator tested;
   @Mocked ServiceRegistration serviceRegistrationMock;
   @Mocked final IdGenerator unused = null;

   @Before
   public void setUp()
   {
      tested = new ServiceRegistrator();
   }

   @Test
   public void testRegisterService(final BundleContext bundleContextMock)
   {
      // Data for the test:
      final String name = "a name";
      final Object serviceImpl = new Object();
      final long expectedId = 42;

      new Expectations()
      {
         {
            // Inject one of the mocks into the tested object:
            setField(tested, bundleContextMock);

            bundleContextMock.registerService(name, serviceImpl, null);
            result = serviceRegistrationMock;

            IdGenerator.generateNewId(); result = expectedId;
         }
      };

      // Code under test is exercised (replay phase):
      long actualId = tested.registerService(name, serviceImpl);

      // No need to tell JMockit to verify missing expectations, since it's done automatically.

      // State-based verifications (simplified):
      Map<Long, ServiceRegistration> serviceRegistrations = getField(tested, Map.class);

      assertEquals(1, serviceRegistrations.size());
      assertSame(serviceRegistrationMock, serviceRegistrations.get(actualId));
   }

   @Test
   public void testUnregisterService()
   {
      final Map<Long, ServiceRegistration> serviceRegistrations =
         new HashMap<Long, ServiceRegistration>();
      long id = 1L;
      serviceRegistrations.put(id, serviceRegistrationMock);

      new Expectations()
      {
         {
            setField(tested, serviceRegistrations);

            serviceRegistrationMock.unregister();
         }
      };

      tested.unregisterService(id);

      assertTrue(serviceRegistrations.isEmpty());
   }

   @Test(expected = IllegalStateException.class)
   public void testUnregisterServiceWithIdWhichDoesntExist()
   {
      // No invocation on any mock is expected.
      new Expectations() {};

      tested.unregisterService(1L);
   }
}
