/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package mockit.integration.junit4;

import static org.junit.Assert.*;
import org.junit.*;

import mockit.*;

public final class MockDependencyTest
{
   @Mocked Dependency mock;

   @Test
   public void useMockedDependency()
   {
      assertFalse(Dependency.alwaysTrue());
   }
}
