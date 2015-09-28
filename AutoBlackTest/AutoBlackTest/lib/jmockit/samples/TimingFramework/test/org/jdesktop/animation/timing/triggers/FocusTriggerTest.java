/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.triggers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;

public final class FocusTriggerTest
{
   @Test
   public void testAddTrigger()
   {
      Component button = new JButton("Test");

      FocusTrigger trigger = FocusTrigger.addTrigger(button, null, FocusTriggerEvent.IN);

      FocusListener[] focusListeners = button.getFocusListeners();
      assertSame(trigger, focusListeners[focusListeners.length - 1]);
   }

   @Test
   public void testAddTriggerWithAutoReverse()
   {
      Component label = new JLabel();

      FocusTrigger trigger = FocusTrigger.addTrigger(label, null, FocusTriggerEvent.IN, true);

      assertSame(trigger, label.getFocusListeners()[0]);
   }

   @Test
   public void testFocusGained()
   {
      final FocusTrigger focusTrigger = new FocusTrigger(null, FocusTriggerEvent.IN);

      new Expectations(Trigger.class)
      {
         {
            focusTrigger.fire(FocusTriggerEvent.IN);
         }
      };

      focusTrigger.focusGained(null);
   }

   @Test
   public void testFocusLost()
   {
      final FocusTrigger focusTrigger = new FocusTrigger(null, FocusTriggerEvent.OUT);

      new Expectations(Trigger.class)
      {
         {
            focusTrigger.fire(FocusTriggerEvent.OUT);
         }
      };

      focusTrigger.focusLost(null);
   }
}
