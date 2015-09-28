/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing;

import mockit.*;

import org.junit.*;

public final class TimingSourceTest
{
   @Mocked private TimingEventListener timingEventListener;
   private TimingSource source;

   @Before
   public void setUp()
   {
      source = new TestTimingSource();
   }

   private static final class TestTimingSource extends TimingSource
   {
      @Override
      public void start()
      {}

      @Override
      public void stop()
      {}

      @Override
      public void setResolution(int resolution)
      {}

      @Override
      public void setStartDelay(int delay)
      {}
   }

   @Test
   public void testAddEventListener()
   {
      new Expectations()
      {
         {
            timingEventListener.timingSourceEvent(source);
         }
      };

      source.addEventListener(timingEventListener);
      source.timingEvent();
   }

   @Test
   public void testRemoveEventListener()
   {
      source.addEventListener(timingEventListener);

      // Expects nothing.

      source.removeEventListener(timingEventListener);
      source.timingEvent();

      new FullVerifications() {};
   }
}
