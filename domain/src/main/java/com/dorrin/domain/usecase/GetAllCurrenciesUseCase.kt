package com.dorrin.domain.usecase

import com.dorrin.domain.model.Currency
import io.reactivex.rxjava3.core.Single

interface GetAllCurrenciesUseCase {
  operator fun invoke(): Single<List<Currency>>
}