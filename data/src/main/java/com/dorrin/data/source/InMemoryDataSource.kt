package com.dorrin.data.source

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.entities.DateTimeEntity
import kotlin.random.Random

internal class InMemoryDataSource : DataSource {
  private val allCurrencies = listOf(
    CurrencyEntity("USD", "United States Dollars", "United States"),
    CurrencyEntity("EUR", "Euro", "Europe"),
    CurrencyEntity("GBP", "Great Britain Pounds", "Great Britain"),
    CurrencyEntity("CAD", "Canadian Dollars", "Canada"),
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