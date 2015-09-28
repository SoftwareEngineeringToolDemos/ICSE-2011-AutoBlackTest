/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.*;

class BaseClass
{
   protected int baseInt;
   protected String baseString;
   protected Set<Boolean> baseSet;
   private long longField;

   static void doStatic1() { throw new RuntimeException("Real method 1 called"); }
   static void doStatic2() { throw new RuntimeException("Real method 2 called"); }

   void doSomething1() { throw new RuntimeException("Real method 1 called"); }
   void doSomething2() { throw new RuntimeException("Real method 2 called"); }

   void setLongField(long value) { longField = value; }
}
