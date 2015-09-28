/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package fakingXmocking;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import org.junit.*;

import mockit.*;

public final class CurrencyConversion2Test
{
   String conversionHtml = "<div id=\"converter_results\"><ul><li><b>1 X = 1.3 Y</b>";
   @Tested CurrencyConversion2 conversion;

   @Test
   public void getAllCurrencySymbols() throws Exception
   {
      new Expectations() {
         URL url;

         {
            new URL(withMatch(".+by_currency.*")).openStream();
            result =
               "<h3>Currency Data</h3>\r\n" +
               " <td valign=top>ABC</td>\r\n" +
               " <td valign=top>XYZ</td>";
         }
      };

      List<String> allSymbols = conversion.currencySymbols();

      assertEquals(asList("ABC", "XYZ"), allSymbols);
   }

   // A reusable expectation block.
   final class CurrencySymbolsExpectations extends NonStrictExpectations
   {
      CurrencySymbolsExpectations()
      {
         super(conversion); // partial mocking of the tested object

         conversion.currencySymbols();
         returns("X", "Y");
      }
   }

   @Test
   public void convertFromOneCurrencyToAnother() throws Exception
   {
      new CurrencySymbolsExpectations();

      new Expectations() {
         URL url;

         {
            new URL(withMatch(".+&from=X&to=Y.*")).openStream();
            result = conversionHtml;
         }
      };

      BigDecimal rate = conversion.convertFromTo("X", "Y");

      assertEquals("1.3", rate.toPlainString());
   }

   @Test
   public void convertFromOneCurrencyToAnother_shorterButIncomplete()
   {
      // A more complicated HTML fragment, for some variation.
      conversionHtml =
         "<h4>Conversion rate</h4>\r\n" +
         "<div id=\"converter_results\"><ul><li>\r\n" +
         "   <b>1 X = 0.15 Y</b>\r\n" +
         "</li></ul></div>";

      new CurrencySymbolsExpectations();

      // Note that this test does not verify proper creation of the URL, nor that the
      // remote site actually gets accessed.
      new Expectations() {
         @Mocked URL url;
         @Input InputStream content = new ByteArrayInputStream(conversionHtml.getBytes());
      };

      BigDecimal rate = conversion.convertFromTo("X", "Y");

      assertEquals("0.15", rate.toPlainString());
   }
}
