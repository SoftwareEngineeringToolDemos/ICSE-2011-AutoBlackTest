/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jdesktop.animation.timing.interpolation;

import java.awt.*;

import org.junit.*;

import mockit.*;

import org.jdesktop.animation.timing.*;
import static org.junit.Assert.*;

public final class PropertySetterTest
{
   private static final class Animated
   {
      private int value;

      Animated() {}
      Animated(int value) { this.value = value; }

      public int getValue() { return value; }
      public void setValue(int value) { this.value = value; }
   }

   @Test
   public void testPropertySetterFromValuesOfReferenceType()
   {
      new PropertySetter<Point>(new Animated(), "value", new Point(0, 1), new Point(2, 3));
   }

   @Test
   public void testCreateAnimatorWithGivenParams()
   {
      int duration = 500;

      Animator animator = PropertySetter.createAnimator(duration, new Animated(), "value", 1, 3);

      assertEquals(duration, animator.getDuration());
   }

   @Test
   public void testCreateAnimatorWithGivenEvaluatorAndParams()
   {
      int duration = 500;
      Evaluator<Double> evaluator = Evaluator.create(Double.class);

      Animator animator = PropertySetter.createAnimator(duration, new Animated(), "value", evaluator, 1.0, 3.0);

      assertEquals(duration, animator.getDuration());
   }

   @Test
   public void testCreateAnimatorWithGivenKeyFrames()
   {
      int duration = 500;
      KeyFrames<Integer> keyFrames = new KeyFrames<Integer>(KeyValues.create(0, 2, 4));

      Animator animator = PropertySetter.createAnimator(duration, new Animated(), "value", keyFrames);

      assertEquals(duration, animator.getDuration());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateAnimatorWithNonExistentProperty()
   {
      PropertySetter.createAnimator(10, new Animated(), "none", 3);
   }

   @Test
   public void testBegin()
   {
      TimingTarget setter = new PropertySetter<Integer>(new Animated(), "value", 1, 3);

      setter.begin();
   }

   @Test
   public void testBeginOnToAnimation()
   {
      final KeyValues<Integer> keyValues = KeyValues.create(3);
      final Integer startValue = 2;
      final Animated animated = new Animated(startValue);
      PropertySetter<Integer> setter = new PropertySetter<Integer>(animated, "value", new KeyFrames<Integer>(keyValues));

      new Expectations(KeyValues.class, Animated.class)
      {
         {
            keyValues.isToAnimation(); result = true;
            animated.getValue(); result = startValue;
            keyValues.setStartValue(startValue);
         }
      };

      setter.begin();
   }

   @Test(expected = RuntimeException.class)
   public void testBeginOnToAnimationWithFailingProperty(final Animated animated)
   {
      new Expectations()
      {
         {
            animated.getValue(); result = new IllegalStateException("test");
         }
      };

      TimingTarget setter = new PropertySetter<Integer>(animated, "value", 3);
      setter.begin();
   }

   @Test
   public void testTimingEvent()
   {
      TimingTarget setter = new PropertySetter<Integer>(new Animated(), "value", 1, 3);

      setter.timingEvent(0.5f);
   }

   @Test(expected = RuntimeException.class)
   public void testTimingEventWithFailingProperty(final Animated animated)
   {
      new Expectations()
      {
         {
            animated.setValue(anyInt); result = new IllegalStateException("test");
         }
      };

      TimingTarget setter = new PropertySetter<Integer>(animated, "value", 1, 3);
      setter.timingEvent(0.1f);
   }
}
