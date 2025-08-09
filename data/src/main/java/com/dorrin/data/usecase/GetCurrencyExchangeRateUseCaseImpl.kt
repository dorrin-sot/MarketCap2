package com.dorrin.data.usecase

import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetCurrencyExchangeRateUseCaseImpl @Inject constructor(
  private val repository: CurrencyExchangeRateRepository
) : GetCurrencyExchangeRateUseCase {
  override fun invoke(from: Currency, to: Currency): Observable<CurrencyExchangeRate> =
    repository.fetchExchangeRate(from, to)
}