/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.interpolation;

import org.junit.*;

import static org.junit.Assert.*;

public final class KeyValuesTest
{
   @Test
   public void testCreateKeyValues()
   {
      KeyValues<Integer> keyValues = KeyValues.create(1, 2, 3);

      assertEquals(3, keyValues.getSize());
      assertSame(Integer.class, keyValues.getType());
      assertFalse(keyValues.isToAnimation());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateKeyValuesWithNullParams()
   {
      //noinspection NullArgumentToVariableArgMethod
      KeyValues.create(null, null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateKeyValuesWithZeroParams()
   {
      Evaluator<Byte> evaluator = null;
      KeyValues.create(evaluator);
   }

   @Test
   public void testSetStartValueOnToAnimation()
   {
      KeyValues<Integer> keyValues = KeyValues.create(5);
      Integer startValue = 2;

      keyValues.setStartValue(startValue);

      Integer value = keyValues.getValue(0, 0, 0);
      assertEquals(startValue, value);
   }
}
