package com.dorrin.domain.usecase

import com.dorrin.data.repository.CurrencyExchangeRateRepository
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.model.mappers.toCurrencyEntity
import com.dorrin.domain.model.mappers.toCurrencyExchangeRate

class GetCurrencyExchangeRateUseCase(
  private val currencyExchangeRateRepository: CurrencyExchangeRateRepository
) {
  operator fun invoke(from: Currency, to: Currency): CurrencyExchangeRate =
    currencyExchangeRateRepository.getExchangeRate(
      from.toCurrencyEntity(),
      to.toCurrencyEntity()
    ).toCurrencyExchangeRate()
}