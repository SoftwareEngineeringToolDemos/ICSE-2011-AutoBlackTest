/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package orderMngr.domain.order;

import java.math.*;

import org.junit.*;

import mockit.*;

import orderMngr.service.*;

/**
 * Unit tests for the OrderRepository class, which depends on the {@link Database} class.
 * The tests use expectations to simulate the interaction between OrderRepository and Database.
 */
public final class OrderRepository_ExpectationsAPI_Test
{
   @Mocked
   final Database db = null; // only contain static methods, so no instance is needed

   Order order;

   @Test
   public void createOrder()
   {
      order = new Order(561, "customer");
      final OrderItem orderItem =
         new OrderItem(order, "Prod", "Some product", 3, new BigDecimal("5.20"));
      order.getItems().add(orderItem);

      new Expectations()
      {
         {
            Database.executeInsertUpdateOrDelete(
               withPrefix("insert into order "), order.getNumber(), order.getCustomerId());
            Database.executeInsertUpdateOrDelete(
               withPrefix("insert into order_item "),
               order.getNumber(), orderItem.getProductId(), orderItem.getProductDescription(),
               orderItem.getQuantity(), orderItem.getUnitPrice());
         }
      };

      new OrderRepository().create(order);
   }

   @Test
   public void updateOrder()
   {
      order = new Order(1, "test");

      new Expectations()
      {
         {
            Database.executeInsertUpdateOrDelete(withPrefix("update order "), order.getCustomerId(), order.getNumber());
         }
      };

      new OrderRepository().update(order);
   }

   @Test
   public void removeOrder()
   {
      order = new Order(35, "remove");

      new Expectations()
      {
         {
            Database.executeInsertUpdateOrDelete(withPrefix("delete from order "), order.getNumber());
         }
      };

      new OrderRepository().remove(order);
   }
}
