/*
 * Copyright (c) 2006-2012 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package org.jmock.samples.parentChild;

import org.junit.*;

import mockit.*;

/**
 * Notice how much simpler the equivalent test is with JMockit.
 */
public final class Child_JMockit_Test
{
   @Mocked Parent parent;
   Child child;

   @Before
   public void createChildOfParent()
   {
      // Expectations can be recorded here, in expectation blocks.
      // If they aren't, all mock invocations during setup will be allowed.

      // Creating the child adds it to the parent.
      child = new Child(parent);
   }

   @Test
   public void removesItselfFromOldParentWhenAssignedNewParent(@Mocked final Parent newParent)
   {
      new Expectations() {{
         parent.removeChild(child);
         newParent.addChild(child);
      }};

      child.reparent(newParent);
   }
}
