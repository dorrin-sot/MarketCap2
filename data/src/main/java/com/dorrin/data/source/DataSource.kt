package com.dorrin.data.source

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity

internal interface DataSource {
  fun getAllCurrencies(): List<CurrencyEntity>

  fun getExchangeRate(from: CurrencyEntity, to: CurrencyEntity): CurrencyExchangeRateEntity
}