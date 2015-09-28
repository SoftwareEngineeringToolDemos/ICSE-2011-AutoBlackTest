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

public final class OrderFactory_ExpectationsAPI_Test
{
   @Test
   public void createOrder() throws Exception
   {
      final String customerId = "123";
      List<OrderItem> expectedItems = asList(
         new OrderItem("393439493", "Core Java 5 6ed", 2, new BigDecimal("45.00")),
         new OrderItem("04940458", "JUnit Recipes", 1, new BigDecimal("49.95")));
      final List<OrderItem> actualItems = new ArrayList<OrderItem>();

      new Expectations()
      {
         @Mocked(methods = "getNumber", inverse = true)
         final Order order = new Order(anyInt, customerId);

         {
            order.getItems(); result = actualItems;
         }

         final OrderRepository orderRepository = new OrderRepository();

         {
            orderRepository.create(order);
         }
      };

      Order order = new OrderFactory().createOrder(customerId, expectedItems);

      assertNotNull(order);
      assertEquals(expectedItems, actualItems);
   }
}
