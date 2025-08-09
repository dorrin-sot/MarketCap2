package com.dorrin.domain.usecase

import com.dorrin.domain.model.Currency
import io.reactivex.rxjava3.core.Observable

interface GetAllCurrenciesUseCase {
  operator fun invoke(): Observable<List<Currency>>
}