package com.dorrin.presentation.conversion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.toLiveData
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import com.dorrin.domain.usecase.GetCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.BackpressureStrategy.LATEST
import io.reactivex.rxjava3.disposables.Disposable
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
internal class ConversionViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
  private val currencyUseCase: GetCurrencyUseCase,
) : ViewModel() {
  val allCurrencies: LiveData<List<CurrencyEntity>> = getAllCurrenciesUseCase()
    .toFlowable(LATEST)
    .toLiveData()

  private var _sourceCurrencyShortName = MutableLiveData<String?>(null)
  val sourceCurrencyShortName: LiveData<String?> get() = _sourceCurrencyShortName

  private var _targetCurrencyShortName = MutableLiveData<String?>(null)
  val targetCurrencyShortName: LiveData<String?> get() = _targetCurrencyShortName

  val sourceCurrency: LiveData<CurrencyEntity> = sourceCurrencyShortName
    .switchMap { shortName ->
      shortName ?: return@switchMap liveData { CurrencyEntity.empty() }

      currencyUseCase(shortName)
        .toFlowable(LATEST)
        .toLiveData()
    }

  val targetCurrency: LiveData<CurrencyEntity> = targetCurrencyShortName
    .switchMap { shortName ->
      shortName ?: return@switchMap liveData { CurrencyEntity.empty() }

      currencyUseCase(shortName)
        .toFlowable(LATEST)
        .toLiveData()
    }

  private val sourceTargetCurrencyPair: LiveData<Pair<CurrencyEntity, CurrencyEntity>> =
    sourceCurrency.switchMap { source -> targetCurrency.map { target -> source to target } }

  private val conversion: LiveData<CurrencyExchangeRateEntity> = sourceTargetCurrencyPair
    .switchMap { (source, target) ->
      if (source.isEmpty() || target.isEmpty())
        return@switchMap liveData { CurrencyExchangeRateEntity.empty() }

      currencyExchangeRateUseCase(source, target)
        .toFlowable(BackpressureStrategy.DROP)
        .toLiveData()
    }

  val rate = conversion.map { DecimalFormat("#,###.##").format(it.rate) }

  val updateTime = conversion.map { "As of ${it.time}" }

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

  fun swapCurrencies() {
    val source = sourceCurrencyShortName.value
    val target = targetCurrencyShortName.value
    _sourceCurrencyShortName.value = target
    _targetCurrencyShortName.value = source
  }
}