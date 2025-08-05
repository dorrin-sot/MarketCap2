package com.dorrin.presentation.conversion

import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate

internal class ConversionPresenter {
  var allCurrencies: List<Currency> = mutableListOf()
    internal set

  val allCurrencyLongNames get() = allCurrencies.toList().map { it.longName }

  var sourceCurrency: Currency? = null
    internal set
  val sourceCurrencyLongName get() = sourceCurrency?.longName ?: ""
  val sourceCurrencyShortName get() = sourceCurrency?.shortName ?: ""

  var targetCurrency: Currency? = null
    internal set
  val targetCurrencyLongName get() = targetCurrency?.longName ?: ""
  val targetCurrencyShortName get() = targetCurrency?.shortName ?: ""

  var conversion: CurrencyExchangeRate? = null
    internal set
  val rate get() = "%.2f".format(conversion?.rate ?: 0f)
  val showConversionDisplay get() = conversion != null
  val conversionDisplay get() = "1 $sourceCurrencyShortName = $rate $targetCurrencyShortName"
}