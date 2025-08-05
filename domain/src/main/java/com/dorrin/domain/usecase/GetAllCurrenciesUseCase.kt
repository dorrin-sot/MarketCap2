package com.dorrin.domain.usecase

import com.dorrin.domain.model.Currency

interface GetAllCurrenciesUseCase {
  operator fun invoke(): List<Currency>
}