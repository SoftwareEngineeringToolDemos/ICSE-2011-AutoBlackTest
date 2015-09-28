/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package fakingXmocking;

import java.math.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import static org.junit.Assert.*;
import org.junit.*;

import mockit.*;

public final class CurrencyConversionTest
{
   @Capturing // so that any class implementing the base type gets mocked
   @Cascading // so that intermediate objects obtained from chained calls get mocked
   HttpClient httpClient;

   @Mocked
   HttpEntity httpEntity; // provides access to the intermediate (cascaded) object

   @Test
   public void loadCurrencySymbolsFromWebSite() throws Exception
   {
      new Expectations() {{
         httpEntity.getContent();
         result = "<h3>Currency Data\r\n <td valign=top>USD</td>\r\n <td valign=top>EUR</td>";
      }};

      List<String> symbols = CurrencyConversion.currencySymbols();

      assertEquals(Arrays.asList("USD", "EUR"), symbols);
   }

   // A reusable expectation block, in case we were to need it in multiple tests.
   final class CurrencySymbolsExpectations extends NonStrictExpectations
   {
      CurrencySymbolsExpectations()
      {
         super(CurrencyConversion.class); // partial mocking of the class

         CurrencyConversion.currencySymbols();
         returns("X", "Y");
      }
   }

   @Test
   public void convertFromOneCurrencyToAnother() throws Exception
   {
      new CurrencySymbolsExpectations();

      // Why strict expectations? To verify the requirement that a live Web site gets accessed.
      new Expectations() {{
         httpEntity.getContent();
         result = "<div id=\"converter_results\"><ul><li><b>1 X = 1.3 Y</b>";
      }};

      BigDecimal rate = CurrencyConversion.convertFromTo("X", "Y");

      assertEquals("1.3", rate.toPlainString());
   }
}
