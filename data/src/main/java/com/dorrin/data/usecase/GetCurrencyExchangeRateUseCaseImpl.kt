package com.dorrin.data.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class GetCurrencyExchangeRateUseCaseImpl @Inject constructor(
  private val repository: CurrencyExchangeRateRepository,
) : GetCurrencyExchangeRateUseCase {
  override fun invoke(
    from: CurrencyEntity,
    to: CurrencyEntity,
  ): Observable<CurrencyExchangeRateEntity> = repository.fetchExchangeRate(from, to)
}