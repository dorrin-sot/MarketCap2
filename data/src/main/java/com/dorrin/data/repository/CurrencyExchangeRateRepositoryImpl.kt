package com.dorrin.data.repository

import com.dorrin.data.entities.mappers.toCurrencyEntity
import com.dorrin.data.entities.mappers.toCurrencyExchangeRate
import com.dorrin.data.source.DataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CurrencyExchangeRateRepositoryImpl @Inject constructor(
  private val dataSource: DataSource
) : CurrencyExchangeRateRepository {
  override fun fetchExchangeRate(from: Currency, to: Currency): Single<CurrencyExchangeRate> =
    dataSource.getExchangeRate(
      from.toCurrencyEntity(),
      to.toCurrencyEntity()
    ).map { it.toCurrencyExchangeRate() }
}