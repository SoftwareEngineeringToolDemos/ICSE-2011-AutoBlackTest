/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.internal.startup;

import java.io.*;

import mockit.integration.junit3.internal.*;
import mockit.integration.junit4.internal.*;
import mockit.integration.testng.internal.*;
import mockit.internal.annotations.*;
import mockit.internal.expectations.mocking.*;
import mockit.internal.util.*;

final class JMockitInitialization
{
   private static final String[] NO_STUBBING_FILTERS = {};
   private final StartupConfiguration config;

   JMockitInitialization() throws IOException { config = new StartupConfiguration(); }

   void initialize(boolean initializeTestNG)
   {
      MockedBridge.preventEventualClassLoadingConflicts();
      loadInternalStartupMocksForJUnitIntegration();

      if (initializeTestNG) {
         try { setUpInternalStartupMock(MockTestNG.class); } catch (Error ignored) {}
      }

      loadExternalToolsIfAny();
      stubOutClassesIfAny();
      setUpStartupMocksIfAny();
   }

   private void loadInternalStartupMocksForJUnitIntegration()
   {
      if (setUpInternalStartupMock(TestSuiteDecorator.class)) {
         try {
            setUpInternalStartupMock(JUnitTestCaseDecorator.class);
         }
         catch (VerifyError ignore) {
            // For some reason, this error occurs when running TestNG tests from Maven.
         }

         setUpInternalStartupMock(RunNotifierDecorator.class);
         setUpInternalStartupMock(BlockJUnit4ClassRunnerDecorator.class);
         setUpInternalStartupMock(JUnit4TestRunnerDecorator.class);
      }
   }

   private boolean setUpInternalStartupMock(Class<?> mockClass)
   {
      try {
         new MockClassSetup(mockClass).setUpStartupMock();
         return true;
      }
      catch (TypeNotPresentException ignore) {
         // OK, ignore the startup mock if the necessary third-party class files are not in the classpath.
         return false;
      }
   }

   private void loadExternalToolsIfAny()
   {
      for (String toolClassName : config.externalTools) {
         new ToolLoader(toolClassName).loadTool();
      }
   }

   private void stubOutClassesIfAny()
   {
      for (String stubbing : config.classesToBeStubbedOut) {
         int p = stubbing.indexOf('#');
         String realClassName = stubbing;
         String[] filters = NO_STUBBING_FILTERS;

         if (p > 0) {
            realClassName = stubbing.substring(0, p);
            filters = stubbing.substring(p + 1).split("\\|");
         }

         Class<?> realClass = Utilities.loadClass(realClassName.trim());
         new ClassStubbing(realClass, true, filters).stubOutAtStartup();
      }
   }

   private void setUpStartupMocksIfAny()
   {
      for (String mockClassName : config.mockClasses) {
         Class<?> mockClass = Utilities.loadClass(mockClassName);
         new MockClassSetup(mockClass).setUpStartupMock();
      }
   }
}
