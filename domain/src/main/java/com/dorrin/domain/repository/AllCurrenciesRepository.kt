package com.dorrin.domain.repository

import com.dorrin.domain.entity.CurrencyEntity
import io.reactivex.rxjava3.core.Observable

interface AllCurrenciesRepository {
  fun fetchAllCurrencies(): Observable<List<CurrencyEntity>>
}