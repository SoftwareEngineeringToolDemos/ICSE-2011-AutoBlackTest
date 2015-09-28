/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.transitions.effects;

import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.transitions.*;
import org.junit.*;

import mockit.*;

public final class ScaleTest
{
   @Mocked ComponentState start;
   @Mocked ComponentState end;
   @Mocked final PropertySetter<Integer> propertySetter = null;
   @Mocked Animator animator;
   @Mocked("init") Effect effect;

   @Test
   public void testInit()
   {
      final Scale scale = new Scale(start, end);

      new Expectations()
      {
         {
            animator.addTarget(
               new PropertySetter(scale, "width", start.getWidth(), end.getWidth()));
            animator.addTarget(
               new PropertySetter(scale, "height", start.getHeight(), end.getHeight()));
            effect.init(animator, null);
         }
      };

      scale.init(animator, null);
   }

   @Test
   public void testInitWithParentEffect()
   {
      final Unchanging parentEffect = new Unchanging();
      Scale scale = new Scale(start, end);

      new Expectations()
      {
         {
            animator.addTarget(
               new PropertySetter(parentEffect, "width", start.getWidth(), end.getWidth()));
            animator.addTarget(
               new PropertySetter(parentEffect, "height", start.getHeight(), end.getHeight()));
            effect.init(animator, null);
         }
      };

      scale.init(animator, parentEffect);
   }

   @Test
   public void testCleanup()
   {
      new Expectations()
      {
         {
            animator.removeTarget(null);
            animator.removeTarget(null);
         }
      };

      new Scale().cleanup(animator);
   }
}
