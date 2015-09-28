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

import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.message.*;

import mockit.*;

@MockClass(realClass = AbstractHttpClient.class, instantiation = Instantiation.PerMockSetup)
public final class CurrencyConversionHttpClientFake
{
   private static final BigDecimal DEFAULT_RATE = new BigDecimal("1.2");
   private final Map<String, BigDecimal> currenciesAndRates = new ConcurrentHashMap<String, BigDecimal>();

   @Mock
   public HttpResponse execute(HttpUriRequest req)
   {
      URI uri = req.getURI();
      final String response;

      if ("www.jhall.demon.co.uk".equals(uri.getHost())) {
         response =
            "<h3>Currency Data</h3>\r\n" +
            "<table><tr>\r\n" +
            "  <td valign=top>USD</td>\r\n" +
            "  <td valign=top>EUR</td>\r\n" +
            "  <td valign=top>BRL</td>\r\n" +
            "  <td valign=top>CNY</td>\r\n" +
            "</tr></table>";
      }
      else {
         String[] params = uri.getQuery().split("&");
         response = formatResultContainingCurrencyConversion(params);
      }

      return new BasicHttpResponse(req.getProtocolVersion(), 200, "OK")
      {
         @Override
         public HttpEntity getEntity() { return createHttpResponse(response); }
      };
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

   private HttpEntity createHttpResponse(String content)
   {
      try {
         return new StringEntity(content);
      }
      catch (UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
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
