/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.logging;

import java.lang.reflect.*;

import org.junit.*;

import mockit.*;
import mockit.integration.logging.*;

import org.apache.commons.logging.*;
import static org.junit.Assert.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/jcl/src/test/java/org/powermock/examples/JclUserTest.java">PowerMock version</a>
 */
@UsingMocksAndStubs(CommonsLoggingMocks.class)
public final class JclUser_JMockit_Test
{
   @Test
   public void assertJclMockingWorks()
   {
      JclUser tested = new JclUser();

      Log logger = Deencapsulation.getField(JclUser.class, Log.class);
      assertTrue(Proxy.isProxyClass(logger.getClass()));

      assertEquals("jcl user", tested.getMessage());
   }
}
