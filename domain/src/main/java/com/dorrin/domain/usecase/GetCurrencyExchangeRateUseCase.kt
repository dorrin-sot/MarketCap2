package com.dorrin.domain.usecase

import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate

interface GetCurrencyExchangeRateUseCase {
  operator fun invoke(from: Currency, to: Currency): CurrencyExchangeRate
}