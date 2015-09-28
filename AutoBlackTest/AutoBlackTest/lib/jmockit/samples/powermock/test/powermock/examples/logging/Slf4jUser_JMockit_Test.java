/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.logging;

import java.lang.reflect.*;

import org.junit.*;

import mockit.*;
import mockit.integration.logging.*;

import static org.junit.Assert.*;
import org.slf4j.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/slf4j/src/test/java/demo/org/powermock/examples/Slf4jUserTest.java">PowerMock version</a>
 */
@UsingMocksAndStubs(Slf4jMocks.class)
public final class Slf4jUser_JMockit_Test
{
   @Test
   public void assertSlf4jMockingWorks() throws Exception
   {
      Slf4jUser tested = new Slf4jUser();

      Logger logger = Deencapsulation.getField(Slf4jUser.class, Logger.class);
      assertTrue(Proxy.isProxyClass(logger.getClass()));

      assertEquals("sl4j user", tested.getMessage());
   }
}
