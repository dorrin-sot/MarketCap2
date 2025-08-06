package com.dorrin.data.source

import com.dorrin.data.entities.CountryEntity
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.entities.DateTimeEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlin.random.Random

internal class InMemoryDataSource @Inject constructor() : DataSource {
  private val allCurrencies = listOf(
    CurrencyEntity(0, "USD", "United States Dollars", CountryEntity("US", "United States", "🇺🇸")),
    CurrencyEntity(1, "EUR", "Euro", CountryEntity("EU", "Europe", "🇪🇺")),
    CurrencyEntity(2, "GBP", "Great Britain Pounds", CountryEntity("GB", "Great Britain", "🇬🇧")),
    CurrencyEntity(3, "CAD", "Canadian Dollars", CountryEntity("CA", "Canada", "🇨🇦")),
  )

  override fun getAllCurrencies(): Single<List<CurrencyEntity>> =
    Single.create { it.onSuccess(allCurrencies) }

  override fun getExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity
  ): Single<CurrencyExchangeRateEntity> {
    val randomRate = Random((from to to).hashCode()).nextFloat()
    val time = DateTimeEntity(2025, 8, 4, 20)
    val exchangeRate = CurrencyExchangeRateEntity(from, to, randomRate, time)
    return Single.create { it.onSuccess(exchangeRate) }
  }
}