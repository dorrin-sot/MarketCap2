package com.dorrin.presentation.currency_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
internal class CurrencyListViewModel @Inject constructor(
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
) : ViewModel() {
  private var _allCurrencies = MutableLiveData<List<CurrencyEntity>>()
  val allCurrencies: LiveData<List<CurrencyEntity>> get() = _allCurrencies

  private var _currencyQuery = MutableLiveData<String?>(null)
  val currencyQuery: LiveData<String?> get() = _currencyQuery

  val filteredCurrencies: LiveData<List<CurrencyEntity>> =
    allCurrencies.switchMap { allCurrencies ->
      currencyQuery
        .map { query ->
          allCurrencies.filter { "$it".contains(query ?: "", ignoreCase = true) }
        }
    }.distinctUntilChanged()

  private var getAllCurrenciesObs: Disposable? = null

  init {
    fetchAllCurrencies()
  }

  private fun fetchAllCurrencies() {
    getAllCurrenciesObs?.dispose()
    getAllCurrenciesObs = getAllCurrenciesUseCase()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { _allCurrencies.value = it }
  }

  fun search(query: String?) {
    _currencyQuery.value = query?.ifEmpty { null }
  }

  override fun onCleared() {
    super.onCleared()
    getAllCurrenciesObs?.dispose()
  }
}