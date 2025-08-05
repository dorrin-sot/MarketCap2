package com.dorrin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.source.DataSource
import com.dorrin.domain.repository.AllCurrenciesRepository

internal class AllCurrenciesRepositoryImpl(
  private val dataSource: DataSource
) : AllCurrenciesRepository {
  private val _allCurrencies = MutableLiveData<List<CurrencyEntity>>()
  val allCurrencies: LiveData<List<CurrencyEntity>> get() = _allCurrencies

  override fun fetchAllCurrencies() {
    _allCurrencies.value = dataSource.getAllCurrencies()
  }
}