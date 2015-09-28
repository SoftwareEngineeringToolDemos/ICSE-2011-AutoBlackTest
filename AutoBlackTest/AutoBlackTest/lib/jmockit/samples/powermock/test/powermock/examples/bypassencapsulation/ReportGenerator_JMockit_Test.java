/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package powermock.examples.bypassencapsulation;

import org.junit.*;

import mockit.*;

import static org.junit.Assert.*;
import powermock.examples.bypassencapsulation.nontest.*;

/**
 * <a href="http://code.google.com/p/powermock/source/browse/trunk/examples/DocumentationExamples/src/test/java/powermock/examples/bypassencapsulation/ReportGeneratorTest.java">PowerMock version</a>
 */
public final class ReportGenerator_JMockit_Test
{
   @Test
   public void testGenerateReport()
   {
      final ReportGenerator tested = new ReportGenerator();
      final String reportId = "id";

      new Expectations()
      {
         ReportTemplateService reportTemplateServiceMock;

         {
            setField(tested, reportTemplateServiceMock);

            reportTemplateServiceMock.getTemplateId(reportId); result = "templateId";
         }
      };

      Report actualReport = tested.generateReport(reportId);

      assertEquals(new Report("name"), actualReport);
   }
}
