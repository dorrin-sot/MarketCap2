package com.dorrin.domain.repository

import com.dorrin.domain.model.Currency

interface CurrencyExchangeRateRepository {
  fun fetchExchangeRate(from: Currency, to: Currency)
}