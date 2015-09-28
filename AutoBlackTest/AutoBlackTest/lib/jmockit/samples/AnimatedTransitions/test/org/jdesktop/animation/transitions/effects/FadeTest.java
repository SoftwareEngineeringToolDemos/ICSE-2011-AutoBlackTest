/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.transitions.effects;

import java.awt.*;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.transitions.*;

public final class FadeTest
{
   @Test
   public void testSetup(@Mocked("setComposite") final Graphics2D g2D)
   {
      Fade fade = new FadeOut();
      final float opacity = 0.5f;
      fade.setOpacity(opacity);

      new Expectations()
      {
         @Mocked("setup") Effect effect;

         {
            g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            effect.setup(g2D);
         }
      };

      fade.setup(g2D);
   }
}
