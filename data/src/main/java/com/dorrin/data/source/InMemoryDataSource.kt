package com.dorrin.data.source

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.entities.DateTimeEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class InMemoryDataSource @Inject constructor() : DataSource {
  private val allCurrencies = listOf(
    CurrencyEntity(0, "USD", "United States Dollars"),
    CurrencyEntity(1, "EUR", "Euro"),
    CurrencyEntity(2, "GBP", "Great Britain Pounds"),
    CurrencyEntity(3, "CAD", "Canadian Dollars"),
  )

  override fun getAllCurrencies(): Single<List<CurrencyEntity>> =
    Single.create { it.onSuccess(allCurrencies) }

  override fun getExchangeRate(
    fromShortName: String,
    toShortName: String
  ): Single<CurrencyExchangeRateEntity> {
    val from = allCurrencies.first { it.shortName == fromShortName }
    val to = allCurrencies.first { it.shortName == toShortName }
    val randomRate = java.util.Random((from to to).hashCode().toLong()).nextFloat()
    val time = DateTimeEntity(2025, 8, 4, 20)
    val exchangeRate = CurrencyExchangeRateEntity(from, to, randomRate, time)
    return Single.create { it.onSuccess(exchangeRate) }
  }
}