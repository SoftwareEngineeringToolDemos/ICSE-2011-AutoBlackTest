/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.triggers;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.timing.*;

public final class TriggerTest
{
   @Mocked Animator animator;

   @Test
   public void testFireWithNonRunningAnimator()
   {
      Trigger trigger = new Trigger(animator);

      new Expectations()
      {
         {
            animator.isRunning(); result = false;
            animator.start();
         }
      };

      trigger.fire();
   }

   @Test
   public void testFireWithRunningAnimator()
   {
      Trigger trigger = new Trigger(animator);

      new Expectations()
      {
         {
            animator.isRunning(); result = true;
            animator.stop();
            animator.start();
         }
      };

      trigger.fire();
   }

   @Test
   public void testFireAfterDisarmed()
   {
      Trigger trigger = new Trigger(animator);
      trigger.disarm();

      // Expect nothing.
      new Expectations() {};

      trigger.fire();
      trigger.fire(null);
   }

   @Test
   public void testFireWithTriggerEventAndNonRunningAnimator()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event);

      new Expectations()
      {
         {
            animator.isRunning(); result = false;
            animator.setStartDirection(Animator.Direction.FORWARD);

            // The original Timing Framework "fire(event)" method did an inappropriate call to
            // "fire()", which would then recheck disarming of the trigger and that the animator was
            // not running; writing this test revealed this issue, leading to better production
            // code.
            animator.start();
         }
      };

      trigger.fire(event);
   }

   @Test
   public void testFireWithTriggerEventAndRunningAnimatorNotOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, false);

      new Expectations()
      {
         {
            animator.isRunning(); result = true;
            animator.stop();
            animator.setStartDirection(Animator.Direction.FORWARD);
            animator.start();
         }
      };

      trigger.fire(event);
   }

   @Test
   public void testFireWithTriggerEventAndNonRunningAnimatorOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, true);

      new Expectations()
      {
         {
            animator.isRunning(); result = false;
            animator.setStartFraction(0.0f);
            animator.isRunning(); result = false;
            animator.setStartDirection(Animator.Direction.FORWARD);
            animator.start();
         }
      };

      trigger.fire(event);
   }

   @Test
   public void testFireWithTriggerEventAndRunningAnimatorOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, true);

      new Expectations()
      {
         {
            animator.isRunning(); result = true;

            animator.getTimingFraction();
            float timingFraction = 0.2f;
            result = timingFraction;

            animator.stop();
            animator.setStartFraction(timingFraction);
            animator.isRunning(); result = false;
            animator.setStartDirection(Animator.Direction.FORWARD);
            animator.start();
         }
      };

      trigger.fire(event);
   }

   @Test
   public void testFireWithOppositeTriggerEventAndNonRunningAnimatorNotOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, false);

      new Expectations() {};

      trigger.fire(event.getOppositeEvent());
   }

   @Test
   public void testFireWithOppositeTriggerEventAndNonRunningAnimatorOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, true);

      new Expectations()
      {
         {
            animator.isRunning(); result = false;
            animator.setStartFraction(1.0f - animator.getStartFraction());
            animator.setStartDirection(Animator.Direction.BACKWARD);
            animator.start();
         }
      };

      trigger.fire(event.getOppositeEvent());
   }

   @Test
   public void testFireWithOppositeTriggerEventAndRunningAnimatorOnAutoReverse()
   {
      TriggerEvent event = FocusTriggerEvent.IN;
      Trigger trigger = new Trigger(animator, event, true);

      new Expectations()
      {
         {
            animator.isRunning(); result = true;
            animator.getTimingFraction(); float timingFraction = 0.2f; result = timingFraction;
            animator.stop();
            animator.setStartFraction(timingFraction);
            animator.setStartDirection(Animator.Direction.BACKWARD);
            animator.start();
         }
      };

      trigger.fire(event.getOppositeEvent());
   }
}
