/**
 * Copyright (c) 2005-2006, Sun Microsystems, Inc
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following 
 *     disclaimer in the documentation and/or other materials provided 
 *     with the distribution.
 *   * Neither the name of the TimingFramework project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jdesktop.animation.timing.interpolation;

import java.awt.Color;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used by KeyValues to calculate intermediate values for specific types. This class
 * has built-in support for the following data types: <ul> <li> java.lang.Byte <li> java.lang.Short
 * <li> java.lang.Integer <li> java.lang.Long <li> java.lang.Float <li> java.lang.Double <li>
 * java.awt.Color <li> java.awt.geom.Point2D <li> java.awt.geom.Line2D <li>
 * java.awt.geom.Dimension2D <li> java.awt.geom.Rectangle2D <li> java.awt.geom.RoundRectangle2D <li>
 * java.awt.geom.Ellipse2D <li> java.awt.geom.Arc2D <li> java.awt.geom.QuadCurve2D <li>
 * java.awt.geom.CubicCurve2D </ul>
 *
 * @author Chet
 */
@SuppressWarnings({"ClassReferencesSubclass", "OverlyCoupledClass"})
public abstract class Evaluator<T>
{
   /**
    * Map that holds all registered evaluators.
    */
   private static final Map<Class<?>, Evaluator<?>> impls = new HashMap<Class<?>, Evaluator<?>>();

   /**
    * Static registration of pre-defined evaluators.
    */
   static {
      impls.put(Byte.class, new EvaluatorByte());
      impls.put(Short.class, new EvaluatorShort());
      impls.put(Integer.class, new EvaluatorInteger());
      impls.put(Long.class, new EvaluatorLong());
      impls.put(Float.class, new EvaluatorFloat());
      impls.put(Double.class, new EvaluatorDouble());
      impls.put(Color.class, new EvaluatorColor());
      impls.put(Point2D.class, new EvaluatorPoint2D());
      impls.put(Line2D.class, new EvaluatorLine2D());
      impls.put(Dimension2D.class, new EvaluatorDimension2D());
      impls.put(Rectangle2D.class, new EvaluatorRectangle2D());
      impls.put(RoundRectangle2D.class, new EvaluatorRoundRectangle2D());
      impls.put(Ellipse2D.class, new EvaluatorEllipse2D());
      impls.put(Arc2D.class, new EvaluatorArc2D());
      impls.put(QuadCurve2D.class, new EvaluatorQuadCurve2D());
      impls.put(CubicCurve2D.class, new EvaluatorCubicCurve2D());
   }

   static <T> Evaluator<T> create(Class<?> type)
   {
      for (Class<?> klass : impls.keySet()) {
         if (klass.isAssignableFrom(type)) {
            //noinspection unchecked
            return (Evaluator<T>) impls.get(klass);
         }
      }

      throw new IllegalArgumentException(
         "No Evaluator can be found for type " + type + "; consider using" +
            " different types for your values or supplying a custom Evaluator");
   }

   /**
    * Abstract method to evaluate between two boundary values.  Built-in implementations all use
    * linear parametric evaluation:
    * <pre>
    *      v = v0 + (v1 - v0) * fraction
    * </pre>
    * Extenders of Evaluator will need to override this method and do something similar for their
    * own types.  Note that this mechanism may be used to create non-linear interpolators for
    * specific value types, although it may be simpler to just use the linear/parametric
    * interpolation technique here and perform non-linear interpolation through custom
    * Interpolators rather than perform custom calculations in this method; the point of this class
    * is to allow calculations with new/unknown types, not to provide another mechanism for
    * non-linear interpolation.
    */
   public abstract T evaluate(T v0, T v1, float fraction);
}

final class EvaluatorByte extends Evaluator<Byte>
{
   @Override
   public Byte evaluate(Byte v0, Byte v1, float fraction)
   {
      return (byte) (v0 + (byte) ((v1 - v0) * fraction));
   }
}

final class EvaluatorShort extends Evaluator<Short>
{
   @Override
   public Short evaluate(Short v0, Short v1, float fraction)
   {
      return (short) (v0 + (short) ((v1 - v0) * fraction));
   }
}

final class EvaluatorInteger extends Evaluator<Integer>
{
   @Override
   public Integer evaluate(Integer v0, Integer v1, float fraction)
   {
      return v0 + (int) ((v1 - v0) * fraction);
   }
}

final class EvaluatorLong extends Evaluator<Long>
{
   @Override
   public Long evaluate(Long v0, Long v1, float fraction)
   {
      return v0 + (long) ((v1 - v0) * fraction);
   }
}

final class EvaluatorFloat extends Evaluator<Float>
{
   @Override
   public Float evaluate(Float v0, Float v1, float fraction)
   {
      return v0 + ((v1 - v0) * fraction);
   }
}

final class EvaluatorDouble extends Evaluator<Double>
{
   @Override
   public Double evaluate(Double v0, Double v1, float fraction)
   {
      return v0 + ((v1 - v0) * fraction);
   }
}

final class EvaluatorColor extends Evaluator<Color>
{
   @Override
   public Color evaluate(Color v0, Color v1, float fraction)
   {
      int red = interpolate(v0.getRed(), v1.getRed(), fraction);
      int green = interpolate(v0.getGreen(), v1.getGreen(), fraction);
      int blue = interpolate(v0.getBlue(), v1.getBlue(), fraction);
      int alpha = interpolate(v0.getAlpha(), v1.getAlpha(), fraction);
      Color value = new Color(red, green, blue, alpha);
      return value;
   }

   private int interpolate(int v0, int v1, float fraction)
   {
      //noinspection MagicNumber
      return v0 + (int) ((v1 - v0) * fraction + 0.5f);
   }
}

final class EvaluatorPoint2D extends Evaluator<Point2D>
{
   // REMIND: apply this technique to other classes...
   private Point2D value;

   @Override
   public Point2D evaluate(Point2D v0, Point2D v1, float fraction)
   {
      if (value == null) {
         value = (Point2D) v0.clone();
      }

      double x = v0.getX() + ((v1.getX() - v0.getX()) * fraction);
      double y = v0.getY() + ((v1.getY() - v0.getY()) * fraction);
      value.setLocation(x, y);
      return value;
   }
}

final class EvaluatorLine2D extends Evaluator<Line2D>
{
   @Override
   public Line2D evaluate(Line2D v0, Line2D v1, float fraction)
   {
      double x1 = v0.getX1() + ((v1.getX1() - v0.getX1()) * fraction);
      double y1 = v0.getY1() + ((v1.getY1() - v0.getY1()) * fraction);
      double x2 = v0.getX2() + ((v1.getX2() - v0.getX2()) * fraction);
      double y2 = v0.getY2() + ((v1.getY2() - v0.getY2()) * fraction);
      Line2D value = (Line2D) v0.clone();
      value.setLine(x1, y1, x2, y2);
      return value;
   }
}

final class EvaluatorDimension2D extends Evaluator<Dimension2D>
{
   @Override
   public Dimension2D evaluate(Dimension2D v0, Dimension2D v1, float fraction)
   {
      double w = v0.getWidth() + ((v1.getWidth() - v0.getWidth()) * fraction);
      double h = v0.getHeight() + ((v1.getHeight() - v0.getHeight()) * fraction);
      Dimension2D value = (Dimension2D) v0.clone();
      value.setSize(w, h);
      return value;
   }
}

final class EvaluatorRectangle2D extends Evaluator<Rectangle2D>
{
   @Override
   public Rectangle2D evaluate(Rectangle2D v0, Rectangle2D v1, float fraction)
   {
      double x = v0.getX() + ((v1.getX() - v0.getX()) * fraction);
      double y = v0.getY() + ((v1.getY() - v0.getY()) * fraction);
      double w = v0.getWidth() + ((v1.getWidth() - v0.getWidth()) * fraction);
      double h = v0.getHeight() + ((v1.getHeight() - v0.getHeight()) * fraction);
      Rectangle2D value = (Rectangle2D) v0.clone();
      value.setRect(x, y, w, h);
      return value;
   }
}

final class EvaluatorRoundRectangle2D extends Evaluator<RoundRectangle2D>
{
   @Override
   public RoundRectangle2D evaluate(RoundRectangle2D v0, RoundRectangle2D v1, float fraction)
   {
      double x = v0.getX() + ((v1.getX() - v0.getX()) * fraction);
      double y = v0.getY() + ((v1.getY() - v0.getY()) * fraction);
      double w = v0.getWidth() + ((v1.getWidth() - v0.getWidth()) * fraction);
      double h = v0.getHeight() + ((v1.getHeight() - v0.getHeight()) * fraction);
      double arcw = v0.getArcWidth() + ((v1.getArcWidth() - v0.getArcWidth()) * fraction);
      double arch = v0.getArcHeight() + ((v1.getArcHeight() - v0.getArcHeight()) * fraction);
      RoundRectangle2D value = (RoundRectangle2D) v0.clone();
      value.setRoundRect(x, y, w, h, arcw, arch);
      return value;
   }
}

final class EvaluatorEllipse2D extends Evaluator<Ellipse2D>
{
   @Override
   public Ellipse2D evaluate(Ellipse2D v0, Ellipse2D v1, float fraction)
   {
      double x = v0.getX() + ((v1.getX() - v0.getX()) * fraction);
      double y = v0.getY() + ((v1.getY() - v0.getY()) * fraction);
      double w = v0.getWidth() + ((v1.getWidth() - v0.getWidth()) * fraction);
      double h = v0.getHeight() + ((v1.getHeight() - v0.getHeight()) * fraction);
      Ellipse2D value = (Ellipse2D) v0.clone();
      value.setFrame(x, y, w, h);
      return value;
   }
}

final class EvaluatorArc2D extends Evaluator<Arc2D>
{
   @Override
   public Arc2D evaluate(Arc2D v0, Arc2D v1, float fraction)
   {
      double x = v0.getX() + ((v1.getX() - v0.getX()) * fraction);
      double y = v0.getY() + ((v1.getY() - v0.getY()) * fraction);
      double w = v0.getWidth() + ((v1.getWidth() - v0.getWidth()) * fraction);
      double h = v0.getHeight() + ((v1.getHeight() - v0.getHeight()) * fraction);
      double start = v0.getAngleStart() + ((v1.getAngleStart() - v0.getAngleStart()) * fraction);
      double extent =
         v0.getAngleExtent() + ((v1.getAngleExtent() - v0.getAngleExtent()) * fraction);
      Arc2D value = (Arc2D) v0.clone();
      value.setArc(x, y, w, h, start, extent, v0.getArcType());
      return value;
   }
}

final class EvaluatorQuadCurve2D extends Evaluator<QuadCurve2D>
{
   @Override
   public QuadCurve2D evaluate(QuadCurve2D v0, QuadCurve2D v1, float fraction)
   {
      double x1 = v0.getX1() + ((v1.getX1() - v0.getX1()) * fraction);
      double y1 = v0.getY1() + ((v1.getY1() - v0.getY1()) * fraction);
      double x2 = v0.getX2() + ((v1.getX2() - v0.getX2()) * fraction);
      double y2 = v0.getY2() + ((v1.getY2() - v0.getY2()) * fraction);
      double ctrlx = v0.getCtrlX() + ((v1.getCtrlX() - v0.getCtrlX()) * fraction);
      double ctrly = v0.getCtrlY() + ((v1.getCtrlY() - v0.getCtrlY()) * fraction);
      QuadCurve2D value = (QuadCurve2D) v0.clone();
      value.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
      return value;
   }
}

final class EvaluatorCubicCurve2D extends Evaluator<CubicCurve2D>
{
   @Override
   public CubicCurve2D evaluate(CubicCurve2D v0, CubicCurve2D v1, float fraction)
   {
      double x1 = v0.getX1() + ((v1.getX1() - v0.getX1()) * fraction);
      double y1 = v0.getY1() + ((v1.getY1() - v0.getY1()) * fraction);
      double x2 = v0.getX2() + ((v1.getX2() - v0.getX2()) * fraction);
      double y2 = v0.getY2() + ((v1.getY2() - v0.getY2()) * fraction);
      double ctrlx1 = v0.getCtrlX1() + ((v1.getCtrlX1() - v0.getCtrlX1()) * fraction);
      double ctrly1 = v0.getCtrlY1() + ((v1.getCtrlY1() - v0.getCtrlY1()) * fraction);
      double ctrlx2 = v0.getCtrlX2() + ((v1.getCtrlX2() - v0.getCtrlX2()) * fraction);
      double ctrly2 = v0.getCtrlY2() + ((v1.getCtrlY2() - v0.getCtrlY2()) * fraction);
      CubicCurve2D value = (CubicCurve2D) v0.clone();
      value.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
      return value;
   }
}
