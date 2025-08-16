package com.dorrin.presentation.conversion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
internal class ConversionViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
) : ViewModel() {
  private var _allCurrencies = MutableLiveData<List<CurrencyEntity>>()
  val allCurrencies: LiveData<List<CurrencyEntity>> get() = _allCurrencies

  private var _sourceCurrency = MutableLiveData<CurrencyEntity?>(null)
  val sourceCurrency: LiveData<CurrencyEntity?> get() = _sourceCurrency

  private var _targetCurrency = MutableLiveData<CurrencyEntity?>(null)
  val targetCurrency: LiveData<CurrencyEntity?> get() = _targetCurrency

  private var _conversion = MutableLiveData(CurrencyExchangeRateEntity.empty())
  private val conversion: LiveData<CurrencyExchangeRateEntity> get() = _conversion

  val sourceCurrencyShortName get() = sourceCurrency.map { it?.shortName }
  val targetCurrencyShortName get() = targetCurrency.map { it?.shortName }
  val rate = conversion.map { DecimalFormat("#,###.##").format(it.rate) }

  private var getAllCurrenciesObs: Disposable? = null
  private var currencyExchangeRateObs: Disposable? = null

  init {
    fetchAllCurrencies()
  }

  private fun fetchAllCurrencies() {
    Log.d("ConversionViewModel", "fetchAllCurrencies")
    getAllCurrenciesObs?.dispose()
    getAllCurrenciesObs = getAllCurrenciesUseCase()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        _allCurrencies.value = it

        val empty = CurrencyEntity.empty()
        val source = sourceCurrency.value ?: empty
        val target = targetCurrency.value ?: empty

        if (source == empty.copy(shortName = source.shortName)) selectSourceCurrency(source.shortName)
        if (target == empty.copy(shortName = target.shortName)) selectTargetCurrency(target.shortName)
      }
  }

  fun selectSourceCurrency(position: Int) {
    Log.d("ConversionViewModel", "selectSourceCurrency - $position")
    selectSourceCurrency(allCurrencies.value!![position])
  }

  fun selectTargetCurrency(position: Int) {
    Log.d("ConversionViewModel", "selectTargetCurrency - $position")
    selectTargetCurrency(allCurrencies.value!![position])
  }

  fun selectSourceCurrency(shortName: String) {
    Log.d("ConversionViewModel", "selectSourceCurrency - $shortName")
    findCurrencyByShortName(shortName)?.let { selectSourceCurrency(it) }
  }

  fun selectTargetCurrency(shortName: String) {
    Log.d("ConversionViewModel", "selectTargetCurrency - $shortName")
    findCurrencyByShortName(shortName)?.let { selectTargetCurrency(it) }
  }

  private fun selectTargetCurrency(currency: CurrencyEntity) {
    Log.d("ConversionViewModel", "selectTargetCurrency - $currency")
    _targetCurrency.value = currency
    performConversion()
  }

  private fun selectSourceCurrency(currency: CurrencyEntity) {
    Log.d("ConversionViewModel", "selectSourceCurrency - $currency")
    _sourceCurrency.value = currency
    performConversion()
  }

  private fun findCurrencyByShortName(shortName: String): CurrencyEntity? =
    (allCurrencies.value ?: emptyList())
      .firstOrNull { it.shortName == shortName }

  fun performConversion() {
    val empty = CurrencyEntity.empty()
    val source = sourceCurrency.value ?: empty
    val target = targetCurrency.value ?: empty

    _conversion.value = CurrencyExchangeRateEntity.empty()

    if (source == empty.copy(source.shortName)) return
    if (target == empty.copy(target.shortName)) return

    currencyExchangeRateObs?.dispose()
    currencyExchangeRateObs = currencyExchangeRateUseCase(source, target)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { _conversion.value = it }
  }

  fun swapCurrencies() {
    val source = sourceCurrency.value
    val target = targetCurrency.value
    _sourceCurrency.value = target
    _targetCurrency.value = source
    performConversion()
  }

  override fun onCleared() {
    super.onCleared()
    getAllCurrenciesObs?.dispose()
    currencyExchangeRateObs?.dispose()
  }
}