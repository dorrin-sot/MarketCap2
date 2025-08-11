package com.dorrin.presentation.conversion

import android.annotation.SuppressLint
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
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
internal class ConversionViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
) : ViewModel() {
  private var _allCurrencies = MutableLiveData<List<Currency>>()
  val allCurrencies: LiveData<List<Currency>> get() = _allCurrencies

  private var _sourceCurrency = MutableLiveData<Currency?>(null)
  val sourceCurrency: LiveData<Currency?> get() = _sourceCurrency

  private var _targetCurrency = MutableLiveData<Currency?>(null)
  val targetCurrency: LiveData<Currency?> get() = _targetCurrency

  private var _conversion = MutableLiveData(CurrencyExchangeRate.empty())
  private val conversion: LiveData<CurrencyExchangeRate> get() = _conversion

  val sourceCurrencyLongName get() = sourceCurrency.map { it?.longName }
  val sourceCurrencyShortName get() = sourceCurrency.map { it?.shortName }

  val targetCurrencyLongName get() = targetCurrency.map { it?.longName }
  val targetCurrencyShortName get() = targetCurrency.map { it?.shortName }

  val rate = conversion.map { DecimalFormat("#,###.##").format(it.rate) }

  val conversionDisplayVisibility = conversion.map { if (it.isEmpty()) View.GONE else View.VISIBLE }

  fun fetchAllCurrencies() {
    getAllCurrenciesUseCase() // todo dispose of observable in viewModel::onCleared
      .subscribe { // todo rxjava gives ability to observe or subscribe on main thread so no need for launch {}
        viewModelScope.launch(Dispatchers.Main) {
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

  @SuppressLint("CheckResult")
  fun performConversion() {
    val source = sourceCurrency.value
    val target = targetCurrency.value

    _conversion.value = CurrencyExchangeRate.empty()

    if (source == null || target == null) return

    currencyExchangeRateUseCase(source, target)
      .subscribe {
        viewModelScope.launch(Dispatchers.Main) {
          _conversion.value = it
        }
      }
  }

  fun swapCurrencies() {
    val source = sourceCurrency.value
    val target = targetCurrency.value
    _sourceCurrency.value = target
    _targetCurrency.value = source
    performConversion()
  }
}