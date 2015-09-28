/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.triggers;

import org.jdesktop.animation.timing.*;
import static org.junit.Assert.*;
import org.junit.*;

import mockit.*;

public final class TimingTriggerTest
{
   @Test
   public void testAddTrigger(final Animator source, final Animator target)
   {
      final TimingTriggerEvent event = TimingTriggerEvent.START;

      new Expectations(TimingTrigger.class)
      {
         {
            source.addTarget(new TimingTrigger(target, event, false));
         }
      };

      TimingTrigger triggerAdded = TimingTrigger.addTrigger(source, target, event);

      assertNotNull(triggerAdded);
   }

   @Test
   public void testAddTriggerWithAutoReverse(final Animator source, final Animator target)
   {
      final TimingTriggerEvent event = TimingTriggerEvent.STOP;

      new Expectations(TimingTrigger.class)
      {
         {
            source.addTarget(new TimingTrigger(target, event, true));
         }
      };

      TimingTrigger triggerAdded = TimingTrigger.addTrigger(source, target, event, true);

      assertNotNull(triggerAdded);
   }

   @Test
   public void testBegin()
   {
      final TimingTrigger timingTrigger = new TimingTrigger(null, TimingTriggerEvent.START);

      new Expectations(Trigger.class)
      {
         {
            timingTrigger.fire(TimingTriggerEvent.START);
         }
      };

      timingTrigger.begin();
   }

   @Test
   public void testEnd()
   {
      final TimingTrigger timingTrigger = new TimingTrigger(null, TimingTriggerEvent.STOP);

      new Expectations(Trigger.class)
      {
         {
            timingTrigger.fire(TimingTriggerEvent.STOP);
         }
      };

      timingTrigger.end();
   }

   @Test
   public void testRepeat()
   {
      final TimingTrigger timingTrigger = new TimingTrigger(null, TimingTriggerEvent.REPEAT);

      new Expectations(Trigger.class)
      {
         {
            timingTrigger.fire(TimingTriggerEvent.REPEAT);
         }
      };

      timingTrigger.repeat();
   }

   @Test
   public void testTimingEvent()
   {
      TimingTrigger timingTrigger = new TimingTrigger(null, TimingTriggerEvent.STOP);

      new Expectations()
      {
         // Mocks the classes where methods could potentially be called by mistake;
         // if any call actually happens, the test will fail.
         @Mocked(methods = "timingEvent", inverse = true)
         final TimingTrigger unused = null;
      };

      timingTrigger.timingEvent(0.0f);
   }
}
