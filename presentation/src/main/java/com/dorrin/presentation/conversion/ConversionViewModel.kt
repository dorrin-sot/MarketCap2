package com.dorrin.presentation.conversion

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
    getAllCurrenciesObs?.dispose()
    getAllCurrenciesObs = getAllCurrenciesUseCase()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { _allCurrencies.value = it }
  }

  fun selectSourceCurrency(id: Long) = selectSourceCurrency(findCurrencyById(id))

  fun selectTargetCurrency(id: Long) = selectTargetCurrency(findCurrencyById(id))

  fun selectTargetCurrency(currency: CurrencyEntity) {
    _targetCurrency.value = currency
    performConversion()
  }

  fun selectSourceCurrency(currency: CurrencyEntity) {
    _sourceCurrency.value = currency
    performConversion()
  }

  fun findCurrencyById(id: Long): CurrencyEntity = allCurrencies.value!!.first { it.id == id }

  fun performConversion() {
    val source = sourceCurrency.value
    val target = targetCurrency.value

    _conversion.value = CurrencyExchangeRateEntity.empty()

    if (source == null || target == null) return

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