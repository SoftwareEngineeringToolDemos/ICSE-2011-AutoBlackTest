/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package orderMngr.domain.order;

import java.math.*;
import java.util.ArrayList;
import java.util.*;

import org.junit.*;

import mockit.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

public final class OrderFactory_VerificationsAPI_Test
{
   @Mocked(methods = "getNumber", inverse = true)
   Order order;

   @Mocked
   OrderRepository orderRepository;

   @Test
   public void createOrder() throws Exception
   {
      // Test data:
      List<OrderItem> expectedItems = asList(
         new OrderItem("393439493", "Core Java 5 6ed", 2, new BigDecimal("45.00")),
         new OrderItem("04940458", "JUnit Recipes", 1, new BigDecimal("49.95")));
      final List<OrderItem> actualItems = new ArrayList<OrderItem>();

      new NonStrictExpectations()
      {{
         order.getItems(); result = actualItems;
      }};

      // Exercises code under test:
      final String customerId = "123";
      final Order created = new OrderFactory().createOrder(customerId, expectedItems);

      // Verify that expected invocations (excluding the ones inside a previous Expectations block)
      // actually occurred:
      new Verifications()
      {{
         new Order(anyInt, customerId);
         orderRepository.create(created);
      }};

      // Conventional JUnit state-based verifications:
      assertNotNull(created);
      assertEquals(expectedItems, actualItems);
   }
}
