/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import junit.framework.*;

@SuppressWarnings({
   "WaitWhileNotSynced", "UnconditionalWait", "WaitWithoutCorrespondingNotify", "WaitNotInLoop",
   "WaitOrAwaitWithoutTimeout", "UnusedDeclaration", "deprecation"})
public final class JREMockingTest extends TestCase
{
   public void testMockingOfFile()
   {
      new NonStrictExpectations() {
         File file;

         {
            file.exists(); result = true;
         }
      };

      File f = new File("...");
      assertTrue(f.exists());
   }

   public void testMockingOfCalendar()
   {
      final Calendar calCST = new GregorianCalendar(2010, 4, 15);

      new NonStrictExpectations(Calendar.class) {{
         Calendar.getInstance(TimeZone.getTimeZone("CST")); result = calCST;
      }};

      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
      assertSame(calCST, cal);
      assertEquals(2010, cal.get(Calendar.YEAR));

      assertNotSame(calCST, Calendar.getInstance(TimeZone.getTimeZone("PST")));
   }

   public void testRegularMockingOfAnnotatedJREMethod(Date d) throws Exception
   {
      assertTrue(d.getClass().getDeclaredMethod("parse", String.class).isAnnotationPresent(Deprecated.class));
   }

   public void testDynamicMockingOfAnnotatedJREMethod() throws Exception
   {
      final Date d = new Date();

      new NonStrictExpectations(d) {{
         d.getMinutes(); result = 5;
      }};

      assertEquals(5, d.getMinutes());
      assertTrue(Date.class.getDeclaredMethod("getMinutes").isAnnotationPresent(Deprecated.class));
   }

   // Mocking of native methods ///////////////////////////////////////////////////////////////////////////////////////

   public void testFirstMockingOfNativeMethods() throws Exception
   {
      new Expectations() {
         // First mocking: puts mocked class in cache, knowing it has native methods to re-register.
         @Mocked("sleep")
         final Thread unused = null;
      };

      Thread.sleep(5000);
   }

   public void testSecondMockingOfNativeMethods(@Mocked("isAlive") final Thread mock)
   {
      new Expectations() {{
         // Second mocking: retrieves from cache, no longer knowing it has native methods to re-register.
         mock.isAlive(); result = true;
      }};

      assertTrue(mock.isAlive());
   }

   public void testUnmockedNativeMethods() throws Exception
   {
      Thread.sleep(10);
      assertTrue(System.currentTimeMillis() > 0);
   }

   // See http://www.javaspecialists.eu/archive/Issue056.html
   public static class InterruptibleThread extends Thread
   {
      protected final boolean interruptRequested()
      {
         try {
            Thread.sleep(10);
            return false;
         }
         catch (InterruptedException ignore) {
            interrupt();
            return true;
         }
      }
   }

   public void testInterruptibleThreadShouldResetItsInterruptStatusWhenInterrupted() throws Exception
   {
      final InterruptibleThread t = new InterruptibleThread();

      new Expectations() {
         @Mocked({"sleep", "interrupt"}) final Thread unused = null;

         {
            Thread.sleep(anyLong); result = new InterruptedException();
            onInstance(t).interrupt();
         }
      };

      assertTrue(t.interruptRequested());
   }

   static class ExampleInterruptibleThread extends InterruptibleThread
   {
      boolean terminatedCleanly;

      @Override
      public void run()
      {
         while (true) {
            for (int i = 0; i < 10; i++) {
               if (interruptRequested()) break;
            }

            if (interruptRequested()) break;
         }

         terminatedCleanly = true;
      }
   }

   public void testInterruptionOfThreadRunningNestedLoops() throws Exception
   {
      ExampleInterruptibleThread t = new ExampleInterruptibleThread();
      t.start();
      Thread.sleep(30);
      t.interrupt();
      t.join();
      assertTrue(t.terminatedCleanly);
   }

   // When a native instance method is called on a regular instance, there is no way to execute its real
   // implementation; therefore, dynamic mocking of native methods is not supported.
   public void testDynamicMockingOfNativeMethod(@Injectable final Thread t)
   {
      new NonStrictExpectations() {{
         t.isAlive();

         try {
            result = true;
            fail();
         }
         catch (IllegalStateException ignore) {
            // OK
         }
      }};
   }

   public void testFullMockingOfThread()
   {
      new NonStrictExpectations() {
         Thread t;

         {
            Thread.activeCount();
            result = 123;
         }
      };

      assertEquals(123, Thread.activeCount());

      new Verifications() {{
         new Thread((Runnable) any); times = 0;
      }};
   }

   public void testDynamicMockingOfThread()
   {
      final Thread d = new Thread((Runnable) null);

      new NonStrictExpectations(d) {};

      d.start();
      d.interrupt();

      new Verifications() {{
         d.start(); times = 1;
         d.interrupt();
      }};
   }

   public void testFullAndPartialMockingOfThread(Thread t)
   {
      final Thread d = new Thread((Runnable) null);

      new NonStrictExpectations(d) {};

      new Verifications() {{
         d.interrupt(); times = 0;
      }};
   }

   public void testMockingOfAnnotatedNativeMethod(@Mocked("countStackFrames") Thread mock) throws Exception
   {
      assertTrue(Thread.class.getDeclaredMethod("countStackFrames").isAnnotationPresent(Deprecated.class));
   }

   @Injectable FileOutputStream stream;

   // This interferes with the test runner if regular mocking is applied.
   public void testDynamicMockingOfFileOutputStreamThroughMockField() throws Exception
   {
      new Expectations() {{
         stream.write((byte[]) any);
      }};

      stream.write("Hello world".getBytes());
   }

   public void testStringBuilder()
   {
      new NonStrictExpectations() {
         StringBuilder builder;

         {
            builder.length(); result = 20;
            builder.toString(); result = "test";
         }
      };

      StringBuilder s = new StringBuilder();
      s.append("something");
      assertEquals(20, s.length());
      assertEquals("test", s.toString());
   }

   // Mocking of java.lang.Object methods /////////////////////////////////////////////////////////////////////////////

   final Object lock = new Object();

   void awaitNotification() throws InterruptedException
   {
      synchronized (lock) {
         lock.wait();
      }
   }

   public void testWaitingWithDynamicPartialMocking() throws Exception
   {
      final Object mockedLock = new Object();

      new Expectations(Object.class) {{ mockedLock.wait(); }};

      awaitNotification();
   }

   public void testWaitingWithLocalMockField() throws Exception
   {
      new NonStrictExpectations() {
         Object mockedLock;

         {
            mockedLock.wait(); times = 1;
         }
      };

      awaitNotification();
   }

   // Mocking the Reflection API //////////////////////////////////////////////////////////////////////////////////////

   @Retention(RetentionPolicy.RUNTIME) @interface AnAnnotation { String value(); }
   @Retention(RetentionPolicy.RUNTIME) @interface AnotherAnnotation {}
   enum AnEnum { @AnAnnotation("one") First, @AnAnnotation("two") Second, @AnotherAnnotation Third }

   public void testMockingOfGetAnnotation() throws Exception
   {
      new MockUp<Field>()
      {
         Field it;

         final Map<Object, Annotation> annotationsApplied = new HashMap<Object, Annotation>() {{
            put(AnEnum.First, anAnnotation("1"));
            put(AnEnum.Second, anAnnotation("2"));
         }};

         AnAnnotation anAnnotation(final String value)
         {
            return new AnAnnotation() {
               public Class<? extends Annotation> annotationType() { return AnAnnotation.class; }
               public String value() { return value; }
            };
         }

         @Mock(reentrant = true)
         <T extends Annotation> T getAnnotation(Class<T> annotation) throws IllegalAccessException
         {
            Object fieldValue = it.get(null);
            Annotation value = annotationsApplied.get(fieldValue);
            if (value != null) return (T) value;
            return it.getAnnotation(annotation);
         }
      };

      Field firstField = AnEnum.class.getField(AnEnum.First.name());
      AnAnnotation annotation1 = firstField.getAnnotation(AnAnnotation.class);
      assertEquals("1", annotation1.value());

      Field secondField = AnEnum.class.getField(AnEnum.Second.name());
      AnAnnotation annotation2 = secondField.getAnnotation(AnAnnotation.class);
      assertEquals("2", annotation2.value());

      Field thirdField = AnEnum.class.getField(AnEnum.Third.name());
      assertNull(thirdField.getAnnotation(AnAnnotation.class));
      assertNotNull(thirdField.getAnnotation(AnotherAnnotation.class));
   }

   // Un-mockable JRE classes /////////////////////////////////////////////////////////////////////////////////////////

   public void testAttemptToMockJREClassThatIsNotMockable()
   {
      try {
         new Expectations() { Class<?> mockClass; };
         fail();
      }
      catch (IllegalArgumentException e) {
         assertTrue(e.getMessage().contains("java.lang.Class"));
      }
   }
}
