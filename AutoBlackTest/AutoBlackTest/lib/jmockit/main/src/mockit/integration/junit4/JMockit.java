/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4;

import org.junit.runners.*;
import org.junit.runners.model.*;

import mockit.internal.startup.*;

/**
 * A test runner for <em>JUnit 4.5+</em>, with special modifications to integrate with JMockit.
 * Normally, though, it shouldn't be necessary to use this class.
 * Instead, simply make sure that {@code jmockit.jar} precedes {@code junit-4.n.jar} in the classpath, when running on
 * JDK 1.6+.
 * When running on JDK 1.5, "<code>-javaagent:jmockit.jar</code>" is mandatory and using this class has no effect.
 *
 * @see <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/RunningTests.html">In the Tutorial</a>
 */
public final class JMockit extends BlockJUnit4ClassRunner
{
   static { Startup.initializeIfNeeded(); }

   /**
    * Constructs a new instance of the test runner.
    *
    * @throws InitializationError if the test class is malformed
    */
   public JMockit(Class<?> testClass) throws InitializationError
   {
      super(testClass);
   }
}
