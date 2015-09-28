/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package fakingXmocking;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import mockit.*;

/**
 * The URL class is used internally by the JRE itself, and we don't want to interfere.
 * Therefore, mock methods in this mock class are <em>reentrant</em> so that they can invoke the original
 * implementation through the {@code it} field, whenever real, not fake, behavior is desired.
 */
@MockClass(realClass = URL.class, instantiation = Instantiation.PerMockedInstance)
public final class CurrencyConversionURLFake
{
   private static final BigDecimal DEFAULT_RATE = new BigDecimal("1.2");
   private static final Map<String, BigDecimal> currenciesAndRates = new ConcurrentHashMap<>();
   public URL it;

   @Mock(reentrant = true)
   public InputStream openStream() throws IOException
   {
      String host = it.getHost();
      String response;

      switch (host) {
         case "www.jhall.demon.co.uk":
            response =
               "<h3>Currency Data</h3>\r\n" +
               "<table><tr>\r\n" +
               "  <td valign=top>USD</td>\r\n" +
               "  <td valign=top>EUR</td>\r\n" +
               "  <td valign=top>BRL</td>\r\n" +
               "  <td valign=top>CNY</td>\r\n" +
               "</tr></table>";
            break;
         case "www.gocurrency.com":
            String[] params = it.getQuery().split("&");
            response = formatResultContainingCurrencyConversion(params);
            break;
         default:
            return it.openStream();
      }

      return new ByteArrayInputStream(response.getBytes());
   }

   private String formatResultContainingCurrencyConversion(String[] params)
   {
      String from = getParameter(params, "from");
      String to = getParameter(params, "to");
      BigDecimal rate = findConversionRate(from, to);

      currenciesAndRates.put(from + '>' + to, rate.setScale(2));
      currenciesAndRates.put(to + '>' + from, BigDecimal.ONE.divide(rate, 2, RoundingMode.HALF_UP));

      return "<div id=\"converter_results\"><ul><li><b>1 " + from + " = " + rate + ' ' + to + "</b>";
   }

   private String getParameter(String[] params, String name)
   {
      for (String param : params) {
         int p = param.indexOf('=');

         if (name.equals(param.substring(0, p))) {
            return param.substring(p + 1);
         }
      }

      return null;
   }

   private BigDecimal findConversionRate(String from, String to)
   {
      BigDecimal rate = currenciesAndRates.get(from + '>' + to);

      if (rate != null) {
         return rate;
      }

      // Special cases:
      if (from.equals(to)) {
         rate = BigDecimal.ONE;
      }
      else if ("USD".equals(from) && "CNY".equals(to)) {
         rate = BigDecimal.TEN;
      }
      else if ("CNY".equals(from) && "USD".equals(to)) {
         rate = new BigDecimal("0.1");
      }
      else {
         rate = DEFAULT_RATE;
      }

      return rate;
   }
}
