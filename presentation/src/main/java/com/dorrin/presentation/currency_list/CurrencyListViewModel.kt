package com.dorrin.presentation.currency_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.toLiveData
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy.LATEST
import javax.inject.Inject

@HiltViewModel
internal class CurrencyListViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : ViewModel() {
  val allCurrencies: LiveData<List<CurrencyEntity>> = getAllCurrenciesUseCase()
    .toFlowable(LATEST)
    .toLiveData()

  private var _currencyQuery = MutableLiveData<String?>(null)
  val currencyQuery: LiveData<String?> get() = _currencyQuery

  val filteredCurrencies: LiveData<List<CurrencyEntity>> =
    allCurrencies.switchMap { allCurrencies ->
      currencyQuery
        .map { query ->
          allCurrencies.filter { "$it".contains(query ?: "", ignoreCase = true) }
        }
    }.distinctUntilChanged()

  fun search(query: String?) {
    _currencyQuery.value = query?.ifEmpty { null }
  }
}