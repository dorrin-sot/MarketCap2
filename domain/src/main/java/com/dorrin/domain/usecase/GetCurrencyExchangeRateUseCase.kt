package com.dorrin.domain.usecase

import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import io.reactivex.rxjava3.core.Observable

interface GetCurrencyExchangeRateUseCase {
  operator fun invoke(from: Currency, to: Currency): Observable<CurrencyExchangeRate>
}