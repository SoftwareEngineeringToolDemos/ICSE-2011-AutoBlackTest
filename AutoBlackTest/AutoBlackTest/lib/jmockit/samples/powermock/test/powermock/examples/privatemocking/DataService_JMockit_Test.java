/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.privatemocking;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/privatemocking/DataServiceTest.java">PowerMock version</a>
 */
public final class DataService_JMockit_Test
{
   @Test
   public void testReplaceData()
   {
      final byte[] expectedBinaryData = {42};
      final String expectedDataId = "id";

      final DataService tested = new DataService();

      // Mock only the "modifyData" method.
      new Expectations(tested)
      {
         {
            invoke(tested, "modifyData", expectedDataId, expectedBinaryData);
            result = true;
         }
      };

      assertTrue(tested.replaceData(expectedDataId, expectedBinaryData));
   }

   @Test
   public void testDeleteData() throws Exception
   {
      final String expectedDataId = "id";

      final DataService tested = new DataService();

      // Mock only the "modifyData" method.
      new Expectations(tested)
      {
         {
            invoke(tested, "modifyData", expectedDataId, byte[].class);
            result = true;
         }
      };

      assertTrue(tested.deleteData(expectedDataId));
   }
}
