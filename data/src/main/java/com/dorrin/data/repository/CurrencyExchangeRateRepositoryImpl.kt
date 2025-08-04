package com.dorrin.data.repository

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity

internal class CurrencyExchangeRateRepositoryImpl: CurrencyExchangeRateRepository {
  override fun getExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity
  ): CurrencyExchangeRateEntity {
    TODO("Not yet implemented")
  }
}