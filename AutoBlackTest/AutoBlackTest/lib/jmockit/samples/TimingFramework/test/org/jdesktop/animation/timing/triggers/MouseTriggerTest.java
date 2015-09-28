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

public final class MouseTriggerTest
{
   @Test
   public void testAddTrigger()
   {
      Component button = new JButton("Test");

      MouseTrigger trigger = MouseTrigger.addTrigger(button, null, MouseTriggerEvent.ENTER);

      MouseListener[] mouseListeners = button.getMouseListeners();
      assertSame(trigger, mouseListeners[mouseListeners.length - 1]);
   }

   @Test
   public void testAddTriggerWithAutoReverse()
   {
      Component button = new JButton("Test");

      MouseTrigger trigger = MouseTrigger.addTrigger(button, null, MouseTriggerEvent.ENTER, true);

      MouseListener[] mouseListeners = button.getMouseListeners();
      assertSame(trigger, mouseListeners[mouseListeners.length - 1]);
   }

   @Test
   public void testMouseEntered()
   {
      MouseListener trigger = createMouseTriggerWithExpectations(MouseTriggerEvent.ENTER);
      trigger.mouseEntered(null);
   }

   private MouseListener createMouseTriggerWithExpectations(final MouseTriggerEvent event)
   {
      final MouseTrigger mouseTrigger = new MouseTrigger(null, event);

      new Expectations()
      {
         final Trigger trigger = null;

         {
            mouseTrigger.fire(event);
         }
      };

      return mouseTrigger;
   }

   @Test
   public void testMouseExited()
   {
      MouseListener trigger = createMouseTriggerWithExpectations(MouseTriggerEvent.EXIT);
      trigger.mouseExited(null);
   }

   @Test
   public void testMousePressed()
   {
      MouseListener trigger = createMouseTriggerWithExpectations(MouseTriggerEvent.PRESS);
      trigger.mousePressed(null);
   }

   @Test
   public void testMouseReleased()
   {
      MouseListener trigger = createMouseTriggerWithExpectations(MouseTriggerEvent.RELEASE);
      trigger.mouseReleased(null);
   }

   @Test
   public void testMouseClicked()
   {
      MouseListener trigger = createMouseTriggerWithExpectations(MouseTriggerEvent.CLICK);
      trigger.mouseClicked(null);
   }
}
