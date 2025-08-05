package com.dorrin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.mappers.toCurrencyEntity
import com.dorrin.data.entities.mappers.toCurrencyExchangeRate
import com.dorrin.data.source.DataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.repository.CurrencyExchangeRateRepository

internal class CurrencyExchangeRateRepositoryImpl(
  private val dataSource: DataSource
) : CurrencyExchangeRateRepository {
  private val _currencyPair = MutableLiveData<Pair<CurrencyEntity, CurrencyEntity>>()
  val currencyPair: LiveData<Pair<CurrencyEntity, CurrencyEntity>> get() = _currencyPair

  private val _exchangeRate = MutableLiveData<CurrencyExchangeRate>()
  val exchangeRate: LiveData<CurrencyExchangeRate> get() = _exchangeRate

  override fun fetchExchangeRate(from: Currency, to: Currency) {
    _currencyPair.value = from.toCurrencyEntity() to to.toCurrencyEntity()
    currencyPair.value?.let { (from, to) ->
      _exchangeRate.value = dataSource.getExchangeRate(from, to).toCurrencyExchangeRate()
    }
  }
}