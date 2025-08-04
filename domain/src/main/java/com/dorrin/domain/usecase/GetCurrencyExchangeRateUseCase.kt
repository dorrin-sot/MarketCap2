package com.dorrin.domain.usecase

import com.dorrin.data.repository.CurrencyExchangeRateRepository

class GetCurrencyExchangeRateUseCase(
  private val currencyExchangeRateRepository: CurrencyExchangeRateRepository
) {
  operator fun invoke() = currencyExchangeRateRepository.getExchangeRate()
}