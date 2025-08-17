package com.dorrin.presentation.conversion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import com.dorrin.domain.usecase.GetCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
internal class ConversionViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
  private val currencyUseCase: GetCurrencyUseCase,
) : ViewModel() {
  private var _allCurrencies = MutableLiveData<List<CurrencyEntity>>()
  val allCurrencies: LiveData<List<CurrencyEntity>> get() = _allCurrencies

  private var _sourceCurrencyShortName = MutableLiveData<String?>(null)
  val sourceCurrencyShortName: LiveData<String?> get() = _sourceCurrencyShortName

  private var _targetCurrencyShortName = MutableLiveData<String?>(null)
  val targetCurrencyShortName: LiveData<String?> get() = _targetCurrencyShortName

  private val _sourceCurrency = MutableLiveData<CurrencyEntity?>()
  val sourceCurrency: LiveData<CurrencyEntity?> get() = _sourceCurrency

  private val _targetCurrency = MutableLiveData<CurrencyEntity?>()
  val targetCurrency: LiveData<CurrencyEntity?> get() = _targetCurrency

  private var _conversion = MutableLiveData(CurrencyExchangeRateEntity.empty())
  private val conversion: LiveData<CurrencyExchangeRateEntity> get() = _conversion

  val rate = conversion.map { DecimalFormat("#,###.##").format(it.rate) }

  val updateTime = conversion.map { "As of ${it.time}" }

  private var getAllCurrenciesObs: Disposable? = null
  private var currencyExchangeRateObs: Disposable? = null
  private var sourceCurrencyObs: Disposable? = null
  private var targetCurrencyObs: Disposable? = null

  private val sourceCurrencyShortnameObs = Observer<String?> { shortName ->
    performConversion()
    shortName ?: return@Observer
    sourceCurrencyObs?.dispose()
    sourceCurrencyObs = currencyUseCase(shortName)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        _sourceCurrency.value = it
      }
  }
  private val targetCurrencyShortNameObs = Observer<String?> { shortName ->
    performConversion()
    shortName ?: return@Observer
    targetCurrencyObs?.dispose()
    targetCurrencyObs = currencyUseCase(shortName)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        _targetCurrency.value = it
        performConversion()
      }
  }

  init {
    fetchAllCurrencies()

    sourceCurrencyShortName.observeForever(sourceCurrencyShortnameObs)
    targetCurrencyShortName.observeForever(targetCurrencyShortNameObs)
  }

  private fun fetchAllCurrencies() {
    Log.d("ConversionViewModel", "fetchAllCurrencies")
    getAllCurrenciesObs?.dispose()
    getAllCurrenciesObs = getAllCurrenciesUseCase()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        _allCurrencies.value = it

        sourceCurrencyShortName.value?.let { selectSourceCurrency(it) }
        targetCurrencyShortName.value?.let { selectTargetCurrency(it) }
      }
  }

  fun selectSourceCurrency(position: Int) {
    Log.d("ConversionViewModel", "selectSourceCurrency - $position")
    selectSourceCurrency(allCurrencies.value!![position].shortName)
  }

  fun selectTargetCurrency(position: Int) {
    Log.d("ConversionViewModel", "selectTargetCurrency - $position")
    selectTargetCurrency(allCurrencies.value!![position].shortName)
  }

  fun selectSourceCurrency(shortName: String) {
    Log.d("ConversionViewModel", "selectSourceCurrency - $shortName")
    _sourceCurrencyShortName.value = shortName
  }

  fun selectTargetCurrency(shortName: String) {
    Log.d("ConversionViewModel", "selectTargetCurrency - $shortName")
    _targetCurrencyShortName.value = shortName
  }

  fun performConversion() {
    Log.d(
      "ConversionViewModel",
      "performConversion - ${sourceCurrency.value}, ${targetCurrency.value}"
    )
    _conversion.value = CurrencyExchangeRateEntity.empty()

    val empty = CurrencyEntity.empty()
    val source = sourceCurrency.value ?: empty
    val target = targetCurrency.value ?: empty

    if (source == empty || target == empty) return

    currencyExchangeRateObs?.dispose()
    currencyExchangeRateObs = currencyExchangeRateUseCase(source, target)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { _conversion.value = it }
  }

  fun swapCurrencies() {
    val source = sourceCurrencyShortName.value
    val target = targetCurrencyShortName.value
    _sourceCurrencyShortName.value = target
    _targetCurrencyShortName.value = source
  }

  override fun onCleared() {
    super.onCleared()
    getAllCurrenciesObs?.dispose()
    currencyExchangeRateObs?.dispose()
    sourceCurrencyObs?.dispose()
    targetCurrencyObs?.dispose()
    sourceCurrencyShortName.removeObserver(sourceCurrencyShortnameObs)
    targetCurrencyShortName.removeObserver(targetCurrencyShortNameObs)
  }
}