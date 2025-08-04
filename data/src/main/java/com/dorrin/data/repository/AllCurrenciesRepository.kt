package com.dorrin.data.repository

import com.dorrin.data.entities.CurrencyEntity

internal interface AllCurrenciesRepository {
  fun getAllCurrencies(): List<CurrencyEntity>
}