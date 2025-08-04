package com.dorrin.data.repository

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity

interface CurrencyExchangeRateRepository {
  fun getExchangeRate(from: CurrencyEntity, to: CurrencyEntity): CurrencyExchangeRateEntity
}