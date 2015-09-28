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
 * These tests are the same as those in {@code CurrencyConversionIntegrationTest}, except that they exercise the
 * refactored and modified version of the tested class, {@code CurrencyConversion2}.
 * <p/>
 * To execute these tests without network access, run with "<code>fakingXmocking.CurrencyConversionURLFake</code>" in
 * the "<code>jmockit-mocks</code>" system property.
 */
public final class CurrencyConversion2IntegrationTest
{
   @Before
   public void emptiesTheLocalCache()
   {
      CurrencyConversion2.allCurrenciesCache = null;
   }

   @Test
   public void loadAllCurrencySymbolsFromWebSite()
   {
      List<String> allSymbols = new CurrencyConversion2().currencySymbols();

      assertTrue(allSymbols.containsAll(asList("USD", "EUR", "BRL", "CNY")));
      assertSame(allSymbols, CurrencyConversion2.allCurrenciesCache);
   }

   @Test
   public void recoverCurrencySymbolsFromLocalCache()
   {
      List<String> cachedSymbols = asList("ABC", "XYZ");
      CurrencyConversion2.allCurrenciesCache = cachedSymbols;
      CurrencyConversion2.lastCacheRead = System.currentTimeMillis();

      List<String> allSymbols = new CurrencyConversion2().currencySymbols();

      assertSame(cachedSymbols, allSymbols);
   }

   @Test
   public void loadCurrencySymbolsFromWebSiteUponDetectingExpiredCache()
   {
      CurrencyConversion2.allCurrenciesCache = Collections.emptyList();
      CurrencyConversion2.lastCacheRead = System.currentTimeMillis() - CurrencyConversion2.CACHE_DURATION;

      List<String> allSymbols = new CurrencyConversion2().currencySymbols();

      assertFalse(allSymbols.isEmpty());
      assertSame(allSymbols, CurrencyConversion2.allCurrenciesCache);
      assertEquals(System.currentTimeMillis(), CurrencyConversion2.lastCacheRead, 20);
   }

   @Test(expected = IllegalArgumentException.class)
   public void convertFromInvalidCurrency()
   {
      new CurrencyConversion2().convertFromTo("invalid", "USD");
   }

   @Test(expected = IllegalArgumentException.class)
   public void convertToInvalidCurrency()
   {
      new CurrencyConversion2().convertFromTo("EUR", "invalid");
   }

   @Test
   public void convertToSameCurrency()
   {
      String currency = "USD";

      BigDecimal identityRate = new CurrencyConversion2().convertFromTo(currency, currency);

      assertEquals(BigDecimal.ONE, identityRate);
   }

   @Test
   public void convertFromDollarToCheapCurrency()
   {
      double rate = new CurrencyConversion2().convertFromTo("USD", "CNY").doubleValue();

      assertTrue(rate > 1.0);
   }

   @Test
   public void convertFromOneCurrencyToAnotherAndBack()
   {
      String fromCurrency = "USD";
      String toCurrency = "EUR";

      BigDecimal rate = new CurrencyConversion2().convertFromTo(fromCurrency, toCurrency);
      assertTrue(rate.doubleValue() > 0.0);

      BigDecimal inverseRate = new CurrencyConversion2().convertFromTo(toCurrency, fromCurrency);
      assertTrue(inverseRate.doubleValue() > 0.0);

      assertEquals(1.0, rate.multiply(inverseRate).doubleValue(), 0.005);
   }
}
