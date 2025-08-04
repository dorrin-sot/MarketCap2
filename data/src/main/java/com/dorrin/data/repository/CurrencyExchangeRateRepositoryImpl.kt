package com.dorrin.data.repository

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.source.DataSource

internal class CurrencyExchangeRateRepositoryImpl(
  private val dataSource: DataSource
) : CurrencyExchangeRateRepository {
  override fun getExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity
  ): CurrencyExchangeRateEntity = dataSource.getExchangeRate(from, to)
}