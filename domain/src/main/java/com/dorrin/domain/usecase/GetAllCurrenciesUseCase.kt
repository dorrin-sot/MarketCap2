package com.dorrin.domain.usecase

import com.dorrin.domain.entity.CurrencyEntity
import io.reactivex.rxjava3.core.Observable

interface GetAllCurrenciesUseCase {
  operator fun invoke(): Observable<List<CurrencyEntity>>
}