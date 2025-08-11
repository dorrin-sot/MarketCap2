package com.dorrin.data.source

import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.DateTimeModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class InMemoryDataSource @Inject constructor() : DataSource {
  private val allCurrencies = listOf(
    CurrencyModel(0, "USD", "United States Dollars"),
    CurrencyModel(1, "EUR", "Euro"),
    CurrencyModel(2, "GBP", "Great Britain Pounds"),
    CurrencyModel(3, "CAD", "Canadian Dollars"),
  )

  override fun getAllCurrencies(): Single<List<CurrencyModel>> =
    Single.create { it.onSuccess(allCurrencies) }

  override fun getExchangeRate(
    fromShortName: String,
    toShortName: String
  ): Single<CurrencyExchangeRateModel> {
    val from = allCurrencies.first { it.shortName == fromShortName }
    val to = allCurrencies.first { it.shortName == toShortName }
    val randomRate = java.util.Random((from to to).hashCode().toLong()).nextFloat()
    val time = DateTimeModel(2025, 8, 4, 20)
    val exchangeRate = CurrencyExchangeRateModel(from, to, randomRate, time)
    return Single.create { it.onSuccess(exchangeRate) }
  }
}