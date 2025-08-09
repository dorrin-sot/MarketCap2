package com.dorrin.domain.repository

import com.dorrin.domain.model.Currency
import io.reactivex.rxjava3.core.Observable

interface AllCurrenciesRepository {
  fun fetchAllCurrencies(): Observable<List<Currency>>
}