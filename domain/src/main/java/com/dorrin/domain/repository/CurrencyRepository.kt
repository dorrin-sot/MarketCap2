package com.dorrin.domain.repository

import com.dorrin.domain.entity.CurrencyEntity
import io.reactivex.rxjava3.core.Observable

interface CurrencyRepository {
  fun fetchCurrency(shortName: String): Observable<CurrencyEntity>
}