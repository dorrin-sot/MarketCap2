package com.dorrin.domain.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import io.reactivex.rxjava3.core.Observable

class GetCurrencyExchangeRateUseCase(private val repository: CurrencyExchangeRateRepository) {
  operator fun invoke(from: CurrencyEntity, to: CurrencyEntity): Observable<CurrencyExchangeRateEntity> =
    repository.fetchExchangeRate(from, to)
}