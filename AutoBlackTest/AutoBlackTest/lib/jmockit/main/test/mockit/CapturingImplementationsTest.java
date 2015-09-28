/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit;

import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;
import org.junit.*;

public final class CapturingImplementationsTest
{
   interface ServiceToBeStubbedOut { int doSomething(); }

   // Just to cause any implementing classes to be stubbed out.
   @Capturing ServiceToBeStubbedOut unused;

   static final class ServiceLocator
   {
      @SuppressWarnings("UnusedDeclaration")
      static <S> S getInstance(Class<S> serviceInterface)
      {
         ServiceToBeStubbedOut service = new ServiceToBeStubbedOut()
         {
            public int doSomething() { return 10; }
         };
         //noinspection unchecked
         return (S) service;
      }
   }

   @Test
   public void captureImplementationLoadedByServiceLocator()
   {
      ServiceToBeStubbedOut service = ServiceLocator.getInstance(ServiceToBeStubbedOut.class);
      assertEquals(0, service.doSomething());
   }

   public interface Service { int doSomething(); }
   static final class ServiceImpl implements Service { public int doSomething() { return 1; } }

   @Test
   public void captureImplementationUsingMockField()
   {
      Service service = new ServiceImpl();

      new Expectations() {
         @Capturing Service mock;

         {
            mock.doSomething();
            returns(2, 3);
         }
      };

      assertEquals(2, service.doSomething());
      assertEquals(3, new ServiceImpl().doSomething());
   }

   @Test
   public void captureImplementationUsingMockParameter(@Capturing final Service mock)
   {
      ServiceImpl service = new ServiceImpl();

      new Expectations() {{
         mock.doSomething();
         returns(3, 2);
      }};

      assertEquals(3, service.doSomething());
      assertEquals(2, new ServiceImpl().doSomething());
   }

   public interface AnotherService { int doSomethingElse(); }
   static final class ServiceImpl2 implements AnotherService { public int doSomethingElse() { return 2; } }
   static final class ServiceImpl3 implements AnotherService { public int doSomethingElse() { return 3; } }

   @Test
   public void captureImplementationByClassName(@Capturing(classNames = ".+ServiceImpl2") AnotherService mock)
   {
      assertEquals(0, new ServiceImpl2().doSomethingElse());
      assertEquals(3, new ServiceImpl3().doSomethingElse());
   }

   @Test
   public void captureImplementationByClassNameForAlreadyLoadedClass()
   {
      AnotherService service = new ServiceImpl2();
      assertEquals(2, service.doSomethingElse());

      new Expectations() {
         @NonStrict @Capturing(classNames = ".+ServiceImpl3", inverse = true)
         AnotherService mock;

         {
            mock.doSomethingElse(); returns(3);
         }
      };

      assertEquals(3, service.doSomethingElse());
   }

   @Ignore @Test
   public void captureOnlyTheNextTwoInstances()
   {
      new NonStrictExpectations() {
         @Capturing(maxInstances = 2) AnotherService mock;

         {
            mock.doSomethingElse(); returns(5, 6);
         }
      };

      assertEquals(5, new ServiceImpl2().doSomethingElse());
      assertEquals(6, new ServiceImpl3().doSomethingElse());
      assertEquals(9, new AnotherService() { public int doSomethingElse() { return 9; } }.doSomethingElse());
   }

   public abstract static class AbstractService { protected abstract boolean doSomething(); }

   static final class DefaultServiceImpl extends AbstractService
   {
      @Override
      protected boolean doSomething() { return true; }
   }

   @Test
   public void captureImplementationOfAbstractClass(@Capturing AbstractService mock)
   {
      assertFalse(new DefaultServiceImpl().doSomething());

      assertFalse(new AbstractService()
      {
         @Override
         protected boolean doSomething()
         {
            throw new RuntimeException();
         }
      }.doSomething());
   }

   @Test
   public void captureGeneratedMockSubclass(@Capturing final AbstractService mock1, final AbstractService mock2)
   {
      new NonStrictExpectations() {{
         mock1.doSomething(); result = true;
         mock2.doSomething(); result = false;
      }};

      assertFalse(mock2.doSomething());
      assertTrue(mock1.doSomething());
      assertTrue(new DefaultServiceImpl().doSomething());
   }

   static class AtomicFieldHolder
   {
      final AtomicIntegerFieldUpdater<AtomicFieldHolder> atomicCount =
         AtomicIntegerFieldUpdater.newUpdater(AtomicFieldHolder.class, "count");

      volatile int count;
   }

   @Test
   public void captureClassPreviouslyLoadedByClassLoaderOtherThanContext()
   {
      final AtomicFieldHolder fieldHolder = new AtomicFieldHolder();

      new Expectations() {
         @Capturing AtomicIntegerFieldUpdater<AtomicFieldHolder> mock;

         {
            mock.compareAndSet(fieldHolder, 0, 1); result = false;
         }
      };

      assertFalse(fieldHolder.atomicCount.compareAndSet(fieldHolder, 0, 1));
      assertEquals(0, fieldHolder.count);
   }
}
