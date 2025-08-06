package com.dorrin.presentation.conversion

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase

internal class ConversionViewModel(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
) : ViewModel() {
  private var _allCurrencies = MutableLiveData<List<Currency>>()
  val allCurrencies: LiveData<List<Currency>> get() = _allCurrencies

  private var _sourceCurrency = MutableLiveData<Currency>()
  private val sourceCurrency: LiveData<Currency> get() = _sourceCurrency

  private var _targetCurrency = MutableLiveData<Currency>()
  private val targetCurrency: LiveData<Currency> get() = _targetCurrency

  private var _conversion = MutableLiveData<CurrencyExchangeRate?>()
  private val conversion: LiveData<CurrencyExchangeRate?> get() = _conversion

  val allCurrencyLongNames = allCurrencies.map { it.map { currency -> currency.longName } }

  val sourceCurrencyLongName = sourceCurrency.map { it.longName }
  val sourceCurrencyShortName = sourceCurrency.map { it.shortName }

  val targetCurrencyLongName = targetCurrency.map { it.longName }
  val targetCurrencyShortName = targetCurrency.map { it.shortName }

  val conversionDisplay = conversion.map {
    it ?: return@map ""
    "%d %s = %.2f %s".format(1, it.from.shortName, it.rate, it.to.shortName)
  }
  val conversionDisplayVisibility = conversionDisplay.map {
    if (it.isEmpty()) View.GONE else View.VISIBLE
  }

  @SuppressLint("CheckResult")
  fun fetchAllCurrencies() {
    getAllCurrenciesUseCase()
      .doOnSuccess { _allCurrencies.value = it }
      .doOnError { it.printStackTrace() }
      .retry(3)
  }

  fun selectSourceCurrency(id: Long) {
    _sourceCurrency.value = allCurrencies.value!!.first { it.id == id }
    performConversion()
  }

  fun selectTargetCurrency(id: Long) {
    _targetCurrency.value = allCurrencies.value!!.first { it.id == id }
    performConversion()
  }

  fun performConversion() {
    (sourceCurrency.value to targetCurrency.value)
      .let { (source, target) ->
        source ?: return@let null
        target ?: return@let null
        return@let source to target
      }?.let { (source, target) -> currencyExchangeRateUseCase(source, target) }
      .also { rate: CurrencyExchangeRate? -> _conversion.value = rate }
  }
}