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

public final class FadeInTest
{
   @Mocked Animator animator;

   @Test
   public void testInit(ComponentState end, @Mocked("init") final Effect effect)
   {
      final FadeIn fadeIn = new FadeIn(end);

      new Expectations(PropertySetter.class)
      {
         {
            animator.addTarget(new PropertySetter(fadeIn, "opacity", 0.0f, 1.0f));
            effect.init(animator, null);
         }
      };

      fadeIn.init(animator, null);
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

      new FadeIn().cleanup(animator);
   }
}
