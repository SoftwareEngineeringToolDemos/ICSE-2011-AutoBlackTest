/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.triggers;

import javax.swing.*;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;

public final class ActionTriggerTest
{
   @Test
   public void testAddTrigger()
   {
      AbstractButton button = new JButton("Test");

      ActionTrigger trigger = ActionTrigger.addTrigger(button, null);

      assertSame(trigger, button.getActionListeners()[0]);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddTriggerFailsOnObjectWithoutAddActionListenerMethod()
   {
      ActionTrigger.addTrigger(new Object(), null);
   }

   @Test
   public void testActionPerformed()
   {
      ActionTrigger actionTrigger = new ActionTrigger(null);

      new Expectations()
      {
         Trigger trigger;

         {
            trigger.fire();
         }
      };

      actionTrigger.actionPerformed(null);
   }
}
