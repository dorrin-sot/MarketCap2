package com.dorrin.data.repository

import androidx.lifecycle.LiveData
import com.dorrin.data.entities.CurrencyEntity

interface AllCurrenciesRepository {
  fun getAllCurrencies(): LiveData<List<CurrencyEntity>>
}