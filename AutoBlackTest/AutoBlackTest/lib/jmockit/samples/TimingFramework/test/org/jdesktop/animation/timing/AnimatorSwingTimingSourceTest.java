/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing;

import java.awt.event.*;
import javax.swing.*;

import org.junit.*;
import static org.junit.Assert.*;

import mockit.*;

public final class AnimatorSwingTimingSourceTest
{
   private ActionListener timerTarget;

   @Test
   public void timingSourceEventOnSwingTimingSourceForRunningAnimator(final TimingTarget timingTarget)
   {
      new MockTimer();

      final Animator animator = new Animator(50);

      new Expectations()
      {
         @Mocked("nanoTime") final System system = null;

         {
            animator.addTarget(timingTarget);

            System.nanoTime(); returns(0L, 50L * 1000000);

            timingTarget.begin();
            timingTarget.timingEvent(1.0f);
            timingTarget.end();
         }
      };

      animator.start();
      timerTarget.actionPerformed(null);

      // Exercise other methods of the SwingTimingSource to fully cover the code, verifying through MockTimer.
      animator.setResolution(10);
      animator.setStartDelay(0);
   }

   class MockTimer extends MockUp<Timer>
   {
      @Mock(invocations = 1) // invocation from Animator(d)
      void $init(int delay, ActionListener actionListener)
      {
         assertEquals(20, delay); // 20 is the initial Animator resolution
         assertNotNull(actionListener);
         timerTarget = actionListener;
      }

      @Mock(invocations = 1)
      void start() {}

      @Mock(invocations = 1)
      void stop() {}

      @Mock(invocations = 1) // invocation from animator.setResolution
      void setDelay(int delay)
      {
         assertEquals(10, delay);
      }

      @Mock(invocations = 2) // one invocation from Animator(d), another from animator.setStartDelay
      void setInitialDelay(int initialDelay)
      {
         assertEquals(0, initialDelay);
      }
   }
}
