/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import static org.junit.Assert.*;
import org.junit.*;

@SuppressWarnings({"deprecation"})
public final class StubOutTest
{
   static class RealClass
   {
      private static final boolean staticInitializerExecuted;
      static { staticInitializerExecuted = true; }

      int doSomething() { return 1; }
      private static void tryAndFail(String s) { throw new AssertionError(s); }

      @Deprecated long getLongValue() { return 15L; }
      static float getFloatValue() { return 1.5F; }
      synchronized double getDoubleValue() { return 1.5; }

      int[][] getMatrix() { return null; }
      boolean[] getBooleans() { return null; }
      char[] getChars() { return null; }
      byte[] getBytes() { return null; }
      short[] getShorts() { return null; }
      int[] getInts() { return null; }
      long[] getLongs() { return null; }
      float[] getFloats() { return null; }
      double[] getDoubles() { return null; }
      Object[] getObjects() { return null; }
   }

   @Test
   public void stubOutMethodsAndStaticInitializer()
   {
      Mockit.stubOut(RealClass.class);

      assertFalse(RealClass.staticInitializerExecuted);

      RealClass obj = new RealClass();
      assertEquals(0, obj.doSomething());
      assertEquals(0L, obj.getLongValue());
      assertEquals(0.0F, RealClass.getFloatValue(), 0);
      assertEquals(0.0, obj.getDoubleValue(), 0);
      assertArrayEquals(new int[0][0], obj.getMatrix());
      assertEquals(0, obj.getBooleans().length);
      assertArrayEquals(new char[0], obj.getChars());
      assertArrayEquals(new byte[0], obj.getBytes());
      assertArrayEquals(new short[0], obj.getShorts());
      assertArrayEquals(new int[0], obj.getInts());
      assertArrayEquals(new long[0], obj.getLongs());
      assertArrayEquals(new float[0], obj.getFloats(), 0);
      assertArrayEquals(new double[0], obj.getDoubles(), 0);
      assertArrayEquals(new Object[0], obj.getObjects());

      RealClass.tryAndFail("test");
   }

   static final class AnotherRealClass
   {
      AnotherRealClass() { throw new IllegalStateException("should not happen"); }

      private String getText(boolean b) { return "" + b; }
   }

   @Test
   public void stubOutClass()
   {
      Mockit.stubOutClass(AnotherRealClass.class);

      assertNull(new AnotherRealClass().getText(true));
   }

   @Test
   public void stubOutClassByName()
   {
      Mockit.stubOutClass(AnotherRealClass.class.getName());

      assertNull(new AnotherRealClass().getText(true));
   }

   static final class YetAnotherRealClass
   {
      private final int value;

      YetAnotherRealClass() { value = 123; }
      YetAnotherRealClass(int value) { this.value = value; }

      public String doSomething(DoSomething a, AnotherRealClass b)
      {
         a.doSomething();
         return b.getText(false);
      }
   }

   static class DoSomething
   {
      int doSomething() { return 1; }
   }

   @Test
   public void stubOutClassUsingFilters()
   {
      Mockit.stubOutClass(YetAnotherRealClass.class, "doSomething");

      YetAnotherRealClass obj = new YetAnotherRealClass();
      assertEquals(123, obj.value);
      assertNull(obj.doSomething(null, null));
   }

   @Test
   public void stubOutClassUsingFiltersByName()
   {
      Mockit.stubOutClass(YetAnotherRealClass.class.getName(), "doSomething");

      YetAnotherRealClass obj = new YetAnotherRealClass();
      assertEquals(123, obj.value);
      assertNull(obj.doSomething(null, null));
   }

   @Test
   public void stubOutClassUsingInverseFilters()
   {
      Mockit.stubOutClass(AnotherRealClass.class, true, "getText(boolean)");
      Mockit.stubOutClass(YetAnotherRealClass.class, false, "()", "(int)");

      assertEquals(0, new YetAnotherRealClass(45).value);
      YetAnotherRealClass obj = new YetAnotherRealClass();
      assertEquals(0, obj.value);
      assertEquals("false", obj.doSomething(new DoSomething(), new AnotherRealClass()));
   }
   
   @Test
   public void stubOutClassUsingInverseFiltersByName()
   {
      Mockit.stubOutClass(AnotherRealClass.class.getName(), true, "getText(boolean)");
      Mockit.stubOutClass(YetAnotherRealClass.class.getName(), false, "()", "(int)");

      assertEquals(0, new YetAnotherRealClass(45).value);
      YetAnotherRealClass obj = new YetAnotherRealClass();
      assertEquals(0, obj.value);
      assertEquals("false", obj.doSomething(new DoSomething(), new AnotherRealClass()));
   }

   @Test
   public void stubOutClassThenSetUpMockMethodsForIt()
   {
      Mockit.stubOut(AnotherRealClass.class);

      new MockUp<AnotherRealClass>()
      {
         @Mock
         String getText(boolean b) { return b ? "mocked" : "MOCKED"; }
      };

      assertEquals("mocked", new AnotherRealClass().getText(true));
      assertEquals("MOCKED", new AnotherRealClass().getText(false));
   }

   @Test
   public void stubOutAnnotatedMethod() throws Exception
   {
      Mockit.stubOut(RealClass.class);

      assertTrue(RealClass.class.getDeclaredMethod("getLongValue").isAnnotationPresent(Deprecated.class));
   }
}
