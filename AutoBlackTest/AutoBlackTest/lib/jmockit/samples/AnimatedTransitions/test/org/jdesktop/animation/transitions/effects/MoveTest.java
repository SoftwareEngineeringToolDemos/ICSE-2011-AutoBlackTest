/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.transitions.effects;

import java.awt.*;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.transitions.*;

public final class MoveTest
{
   @Mocked Animator animator;

   @Test
   public void testInit(
      final ComponentState start, final ComponentState end,
      @Mocked("init") final Effect effectSuperClass)
   {
      final Move effect = new Move(start, end);

      new Expectations(PropertySetter.class)
      {
         {
            Point startPoint = new Point(start.getX(), start.getY());
            Point endPoint = new Point(end.getX(), end.getY());
            new PropertySetter<Point>(effect, "location", startPoint, endPoint);
            animator.addTarget(withInstanceOf(PropertySetter.class));
            effectSuperClass.init(animator, null);
         }
      };

      effect.init(animator, null);
   }

   @Test
   public void testInitWithParentEffect(
      final ComponentState start, final ComponentState end,
      @Mocked("init") final Effect effectSuperClass)
   {
      Move effect = new Move(start, end);
      final Unchanging parentEffect = new Unchanging();

      new Expectations(PropertySetter.class)
      {
         {
            Point startPoint = new Point(start.getX(), start.getY());
            Point endPoint = new Point(end.getX(), end.getY());
            new PropertySetter<Point>(parentEffect, "location", startPoint, endPoint);
            animator.addTarget(withInstanceOf(PropertySetter.class));
            effectSuperClass.init(animator, null);
         }
      };

      effect.init(animator, parentEffect);
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

      new Move().cleanup(animator);
   }
}
