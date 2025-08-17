package com.dorrin.domain.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.repository.CurrencyRepository
import io.reactivex.rxjava3.core.Observable

class GetCurrencyUseCase(private val repository: CurrencyRepository) {
  operator fun invoke(shortName: String): Observable<CurrencyEntity> =
    repository.fetchCurrency(shortName)
}