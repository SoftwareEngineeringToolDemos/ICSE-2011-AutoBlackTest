package fakingXmocking;

import java.io.*;
import java.math.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/**
 * From <a href="http://schuchert.wikispaces.com/JMockIt.AStoryAboutTooMuchPower">this</a> article.
 */
public final class CurrencyConversion
{
   static final int CACHE_DURATION = 5 * 60 * 1000;
   static List<String> allCurrenciesCache;
   static long lastCacheRead = Long.MAX_VALUE;

   public static List<String> currencySymbols()
   {
      if (allCurrenciesCache != null && System.currentTimeMillis() - lastCacheRead < CACHE_DURATION) {
         return allCurrenciesCache;
      }

      List<String> result = new LinkedList<String>();

      try {
         HttpClient httpClient = new DefaultHttpClient();
         HttpGet httpget = new HttpGet("http://www.jhall.demon.co.uk/currency/by_currency.html");
         HttpResponse response = httpClient.execute(httpget);
         HttpEntity entity = response.getEntity();

         if (entity != null) {
            InputStream inStream = entity.getContent();
            InputStreamReader irs = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(irs);
            String l;
            boolean foundTable = false;

            while ((l = br.readLine()) != null) {
               if (foundTable) {
                  if (l.matches("\\s+<td valign=top>[A-Z]{3}</td>")) {
                     result.add(l.replaceAll(".*top>", "").replaceAll("</td>", ""));
                  }
               }

               if (l.startsWith("<h3>Currency Data"))
                  foundTable = true;
            }
         }
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }

      allCurrenciesCache = result;
      lastCacheRead = System.currentTimeMillis();
      return result;
   }

   public static BigDecimal convertFromTo(String fromCurrency, String toCurrency)
   {
      List<String> valid = currencySymbols();

      if (!valid.contains(fromCurrency)) {
         throw new IllegalArgumentException(String.format("Invalid from currency: %s", fromCurrency));
      }

      if (!valid.contains(toCurrency)) {
         throw new IllegalArgumentException(String.format("Invalid to currency: %s", toCurrency));
      }

      String url =
         String.format("http://www.gocurrency.com/v2/dorate.php?inV=1&from=%s&to=%s&Calculate=Convert",
            fromCurrency, toCurrency);

      try {
         HttpClient httpclient = new DefaultHttpClient();
         HttpGet httpget = new HttpGet(url);
         HttpResponse response = httpclient.execute(httpget);
         HttpEntity entity = response.getEntity();
         StringBuilder result = new StringBuilder();

         if (entity != null) {
            InputStream inStream = entity.getContent();
            InputStreamReader irs = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(irs);
            String l;

            while ((l = br.readLine()) != null) {
               result.append(l);
            }
         }

         String theWholeThing = result.toString();
         int start = theWholeThing.lastIndexOf("<div id=\"converter_results\"><ul><li>");
         String substring = result.substring(start);
         int startOfInterestingStuff = substring.indexOf("<b>") + 3;
         int endOfInterestingStuff = substring.indexOf("</b>", startOfInterestingStuff);
         String interestingStuff = substring.substring(startOfInterestingStuff, endOfInterestingStuff);
         String[] parts = interestingStuff.split("=");
         String value = parts[1].trim().split(" ")[0];
         BigDecimal bottom = new BigDecimal(value);
         return bottom;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
