package com.dorrin.presentation.conversion

internal interface ConversionController {
  fun fetchAllCurrencies()

  fun selectSourceCurrency(id: Long)

  fun selectTargetCurrency(id: Long)

  fun performConversion()
}