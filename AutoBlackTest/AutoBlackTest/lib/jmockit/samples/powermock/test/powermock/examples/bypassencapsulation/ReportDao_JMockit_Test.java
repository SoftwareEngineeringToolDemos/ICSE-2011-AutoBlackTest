/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.bypassencapsulation;

import org.junit.*;

import mockit.*;

import powermock.examples.bypassencapsulation.nontest.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/bypassencapsulation/ReportDaoTest.java">PowerMock version</a>
 */
public final class ReportDao_JMockit_Test
{
   @Test
   public void testDeleteReport()
   {
      final String reportName = "reportName";
      final Report report = new Report(reportName);

      final ReportDao tested = new ReportDao();

      new Expectations(tested) // will mock only the "getReportFromTargetName" method
      {
         // Create a mock of the distributed cache.
         Cache cacheMock;

         {
            // Inject the mock cache into the class being tested.
            setField(tested, cacheMock);

            // Record an expectation for the private method.
            invoke(tested, "getReportFromTargetName", reportName); result = report;

            // Expect the call to invalidate cache.
            cacheMock.invalidateCache(report);
         }
      };

      tested.deleteReport(reportName);
   }
}
