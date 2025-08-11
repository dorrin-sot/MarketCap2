package com.dorrin.domain.repository

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import io.reactivex.rxjava3.core.Observable

interface CurrencyExchangeRateRepository {
  fun fetchExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity,
  ): Observable<CurrencyExchangeRateEntity>
}