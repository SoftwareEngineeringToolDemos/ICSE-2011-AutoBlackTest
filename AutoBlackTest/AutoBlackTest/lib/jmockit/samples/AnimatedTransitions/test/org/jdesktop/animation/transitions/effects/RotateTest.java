/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.transitions.effects;

import java.awt.*;
import javax.swing.*;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.timing.*;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.animation.transitions.*;

public final class RotateTest
{
   @Mocked Animator animator;
   @Mocked({"init", "setup"}) Effect effect;

   @Test
   public void testInit(ComponentState start, ComponentState end)
   {
      final Rotate rotate = new Rotate(start, end, 45, 100, 60);

      new Expectations(PropertySetter.class)
      {
         {
            animator.addTarget(new PropertySetter(rotate, "radians", 0.0, Math.PI / 4));
            effect.init(animator, null);
         }
      };

      rotate.init(animator, null);
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

      new Rotate(0, 0, 0).cleanup(animator);
   }

   @Test
   public void testSetup(@Mocked({"translate", "rotate"}) final Graphics2D g2D)
   {
      JComponent component = new JButton();
      component.setSize(80, 60);
      Rotate rotate = new Rotate(90, component);
      rotate.setRadians(0.2);

      new Expectations()
      {
         {
            g2D.translate(40, 30);
            g2D.rotate(0.2);
            g2D.translate(-40, -30);
            effect.setup(g2D);
         }
      };

      rotate.setup(g2D);
   }
}
