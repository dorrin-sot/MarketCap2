package com.dorrin.data.source

import com.dorrin.data.entities.CountryEntity
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.entities.DateTimeEntity
import kotlin.random.Random

class InMemoryDataSource : DataSource { // todo "internal"
  private val allCurrencies = listOf(
    CurrencyEntity(0, "USD", "United States Dollars", CountryEntity("US", "United States", "🇺🇸")),
    CurrencyEntity(1, "EUR", "Euro", CountryEntity("EU", "Europe", "🇪🇺")),
    CurrencyEntity(2, "GBP", "Great Britain Pounds", CountryEntity("GB", "Great Britain", "🇬🇧")),
    CurrencyEntity(3, "CAD", "Canadian Dollars", CountryEntity("CA", "Canada", "🇨🇦")),
  )

  override fun getAllCurrencies(): List<CurrencyEntity> = allCurrencies

  override fun getExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity
  ): CurrencyExchangeRateEntity {
    val randomRate = Random((from to to).hashCode()).nextFloat()
    val time = DateTimeEntity(2025, 8, 4, 20)
    return CurrencyExchangeRateEntity(from, to, randomRate, time)
  }
}