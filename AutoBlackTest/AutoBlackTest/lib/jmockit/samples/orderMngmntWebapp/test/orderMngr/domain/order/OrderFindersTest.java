/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package orderMngr.domain.order;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.junit.*;

import mockit.*;

import orderMngr.service.*;
import static org.junit.Assert.*;

public final class OrderFindersTest
{
   @NonStrict final Database db = null;
   @Mocked ResultSet rs;
   Order order;

   @Test
   public void findOrderByNumber() throws Exception
   {
      // Set up state.
      order = new Order(1, "test");
      OrderItem orderItem = new OrderItem(order, "343443", "Some product", 3, new BigDecimal(5));
      order.getItems().add(orderItem);

      // Record expectations:
      new Expectations() {
         ResultSet rs2;

         {
            Database.executeQuery("select customer_id from order where number=?", order.getNumber());
            result = rs;

            rs.next(); result = true;
            rs.getString(1); result = order.getCustomerId();

            Database.executeQuery(withMatch("select .+ from order_item where .+"), order.getNumber());
            result = rs2;

            rs2.next(); result = true;
            rs2.getString(1);
            rs2.getString(2);
            rs2.getInt(3);
            rs2.getBigDecimal(4);
            rs2.next(); result = false;
         }
      };

      // Exercise code under test:
      Order found = new OrderRepository().findByNumber(order.getNumber());

      // Verify results:
      assertEquals(order, found);
   }

   @Test
   public void findOrderByCustomer(@Mocked("loadOrderItems") final OrderRepository repository) throws Exception
   {
      final String customerId = "Cust";
      order = new Order(890, customerId);

      new Expectations() {{
         Database.executeQuery(withMatch("select.+from\\s+order.*where.+customer_id\\s*=\\s*\\?"), customerId);
         result = rs;

         rs.next(); result = true;
         rs.getInt(1); result = order.getNumber();
         invoke(repository, "loadOrderItems", order);
         rs.next(); result = false;
      }};

      List<Order> found = repository.findByCustomer(customerId);

      assertTrue("Order not found by customer id", found.contains(order));

      new Verifications() {{ Database.closeStatement(rs); }};
   }
}
