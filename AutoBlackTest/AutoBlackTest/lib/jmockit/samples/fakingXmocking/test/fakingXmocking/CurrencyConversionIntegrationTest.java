/*
 * Copyright (c) 2006-2011 Rogério Liesenfeld
 * This file is subject to the terms of the MIT license (see LICENSE.txt).
 */
package fakingXmocking;

import java.math.*;
import java.util.*;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * These are acceptance/integration tests which verify the full functionality of the {@code CurrencyConversion}
 * class, with actual Web access. Therefore, they can only be executed successfully in a machine with free access
 * to the Web.
 * <p/>
 * That said, the <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/StateBasedTesting.html">JMockit
 * Mockups</a> API provides a convenient way to <em>fake</em> the Internet connection, with simulated Web sites.
 * The code for the fake behavior is implemented in an annotated <em>mock class</em>, which can be set up for a
 * test run through <a href="http://jmockit.googlecode.com/svn/trunk/www/tutorial/UsingMocksAndStubs.html">external
 * configuration</a>.
 * <p/>
 * To execute these tests with fake Web access, run with
 * "<code>-Djmockit-mocks=fakingXmocking.CurrencyConversionHttpClientFake</code>".
 * <p/>
 * This ability can be used, for example, to execute "real" acceptance tests daily in a dedicated build machine,
 * while allowing developers to quickly and safely run the same tests in their development machines.
 */
public final class CurrencyConversionIntegrationTest
{
   @Before
   public void emptiesTheLocalCache()
   {
      CurrencyConversion.allCurrenciesCache = null;
   }

   @Test
   public void loadAllCurrencySymbolsFromWebSite()
   {
      List<String> allSymbols = CurrencyConversion.currencySymbols();

      assertTrue(allSymbols.containsAll(asList("USD", "EUR", "BRL", "CNY")));
      assertSame(allSymbols, CurrencyConversion.allCurrenciesCache);
   }

   @Test
   public void recoverCurrencySymbolsFromLocalCache()
   {
      List<String> cachedSymbols = asList("ABC", "XYZ");
      CurrencyConversion.allCurrenciesCache = cachedSymbols;
      CurrencyConversion.lastCacheRead = System.currentTimeMillis();

      List<String> allSymbols = CurrencyConversion.currencySymbols();

      assertSame(cachedSymbols, allSymbols);
   }

   @Test
   public void loadCurrencySymbolsFromWebSiteUponDetectingExpiredCache()
   {
      CurrencyConversion.allCurrenciesCache = Collections.emptyList();
      CurrencyConversion.lastCacheRead = System.currentTimeMillis() - CurrencyConversion.CACHE_DURATION;

      List<String> allSymbols = CurrencyConversion.currencySymbols();

      assertFalse(allSymbols.isEmpty());
      assertSame(allSymbols, CurrencyConversion.allCurrenciesCache);
      assertEquals(System.currentTimeMillis(), CurrencyConversion.lastCacheRead, 20);
   }

   @Test(expected = IllegalArgumentException.class)
   public void convertFromInvalidCurrency()
   {
      CurrencyConversion.convertFromTo("invalid", "USD");
   }

   @Test(expected = IllegalArgumentException.class)
   public void convertToInvalidCurrency()
   {
      CurrencyConversion.convertFromTo("EUR", "invalid");
   }

   @Test
   public void convertToSameCurrency()
   {
      String currency = "USD";

      BigDecimal identityRate = CurrencyConversion.convertFromTo(currency, currency);

      assertEquals(BigDecimal.ONE, identityRate);
   }

   @Test
   public void convertFromDollarToCheapCurrency()
   {
      double rate = CurrencyConversion.convertFromTo("USD", "CNY").doubleValue();

      assertTrue(rate > 1.0);
   }

   @Test
   public void convertFromOneCurrencyToAnotherAndBack()
   {
      String fromCurrency = "USD";
      String toCurrency = "EUR";

      BigDecimal rate = CurrencyConversion.convertFromTo(fromCurrency, toCurrency);
      assertTrue(rate.doubleValue() > 0.0);

      BigDecimal inverseRate = CurrencyConversion.convertFromTo(toCurrency, fromCurrency);
      assertTrue(inverseRate.doubleValue() > 0.0);

      assertEquals(1.0, rate.multiply(inverseRate).doubleValue(), 0.005);
   }
}
