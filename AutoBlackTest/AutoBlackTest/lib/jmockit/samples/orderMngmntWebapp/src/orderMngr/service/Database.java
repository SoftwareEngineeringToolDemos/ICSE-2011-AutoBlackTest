/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package orderMngr.service;

import java.sql.*;

/**
 * A static facade for database access through JDBC. It assumes the application can use a single
 * global DB connection. (This class is just for the sake of demonstration; in the real world,
 * direct use of JDBC like this is not too practical.)
 */
public final class Database
{
   private static Connection connection;

   public static synchronized Connection connection()
   {
      if (connection == null) {
         try {
            connection = DriverManager.getConnection("jdbc:test:ordersDB");
         }
         catch (SQLException e) {
            throw new RuntimeException(e);
         }
      }

      return connection;
   }

   public static void executeInsertUpdateOrDelete(String sql, Object... args)
   {
      PreparedStatement stmt = null;

      try {
         stmt = createStatement(sql, args);
         stmt.executeUpdate();
      }
      catch (SQLException e) {
         throw new RuntimeException(e);
      }
      finally {
         closeStatement(stmt);
      }
   }

   private static PreparedStatement createStatement(String sql, Object... args) throws SQLException
   {
      PreparedStatement stmt = connection().prepareStatement(sql);
      int i = 1;

      for (Object arg : args) {
         stmt.setObject(i, arg);
         i++;
      }

      return stmt;
   }

   public static void closeStatement(Statement stmt)
   {
      if (stmt != null) {
         try {
            stmt.close();
         }
         catch (SQLException e) {
            throw new RuntimeException(e);
         }
      }
   }

   public static void closeStatement(ResultSet result)
   {
      if (result != null) {
         try {
            result.getStatement().close();
         }
         catch (SQLException e) {
            throw new RuntimeException(e);
         }
      }
   }

   public static ResultSet executeQuery(String sql, Object... args)
   {
      try {
         PreparedStatement stmt = createStatement(sql, args);
         return stmt.executeQuery();
      }
      catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}
