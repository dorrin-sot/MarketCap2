package com.dorrin.presentation.conversion

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ConversionViewModel @Inject constructor(
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

  val sourceCurrencyLongName get() = sourceCurrency.map { it.longName }
  val sourceCurrencyShortName get() = sourceCurrency.map { it.shortName }

  val targetCurrencyLongName get() = targetCurrency.map { it.longName }
  val targetCurrencyShortName get() = targetCurrency.map { it.shortName }

  val conversionDisplay = conversion.map {
    it ?: return@map ""
    "%d %s = %.2f %s".format(1, it.from.shortName, it.rate, it.to.shortName)
  }

  val rate = conversion.map { it?.rate.toString() }

  val conversionDisplayVisibility = conversionDisplay.map {
    if (it.isEmpty()) View.GONE else View.VISIBLE
  }

  fun fetchAllCurrencies() {
    viewModelScope.launch {
      getAllCurrenciesUseCase()
        .retry(3)
        .blockingGet()
        .also {
          _allCurrencies.value = it
        }
    }
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
    val source = sourceCurrency.value
    val target = targetCurrency.value

    if (source == null || target == null) {
      _conversion.value = null
      return
    }

    viewModelScope.launch {
      currencyExchangeRateUseCase(source, target)
        .retry(3)
        .blockingGet()
        .also { _conversion.value = it }
    }
  }
}