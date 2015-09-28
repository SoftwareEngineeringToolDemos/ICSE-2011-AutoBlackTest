/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.transitions.effects;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.transitions.*;

public final class FadeOutTest
{
   @Mocked Animator animator;

   @Test
   public void testInit(ComponentState start, @Mocked("init") final Effect effect)
   {
      final FadeOut fadeOut = new FadeOut(start);

      new Expectations(PropertySetter.class)
      {
         {
            animator.addTarget(new PropertySetter(fadeOut, "opacity", 1.0f, 0.0f));
            effect.init(animator, null);
         }
      };

      fadeOut.init(animator, null);
   }

   @Test
   public void testCleanup()
   {
      new Expectations()
      {
         {
            animator.removeTarget(null);
         }
      };

      new FadeOut().cleanup(animator);
   }
}
