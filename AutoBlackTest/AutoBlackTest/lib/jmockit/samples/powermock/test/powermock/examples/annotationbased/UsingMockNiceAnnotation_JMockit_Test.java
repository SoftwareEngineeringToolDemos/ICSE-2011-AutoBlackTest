/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.annotationbased;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;
import powermock.examples.annotationbased.dao.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/annotationbased/UsingMockNiceAnnotationTest.java">PowerMock version</a>
 */
public final class UsingMockNiceAnnotation_JMockit_Test
{
   @Mocked
   private SomeDao someDaoMock;

   private SomeService someService;

   @Before
   public void setUp()
   {
      someService = new SomeService(someDaoMock);
   }

   @Test
   public void assertThatNiceMockAnnotationWork()
   {
      assertNull(someService.getData());
      assertNull(someService.getMoreData());
   }
}
