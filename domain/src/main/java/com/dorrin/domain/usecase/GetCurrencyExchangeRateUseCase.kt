package com.dorrin.domain.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import io.reactivex.rxjava3.core.Observable

interface GetCurrencyExchangeRateUseCase {
  operator fun invoke(
    from: CurrencyEntity,
    to: CurrencyEntity,
  ): Observable<CurrencyExchangeRateEntity>
}