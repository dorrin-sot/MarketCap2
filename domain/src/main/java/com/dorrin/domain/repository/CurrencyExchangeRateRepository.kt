package com.dorrin.domain.repository

import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import io.reactivex.rxjava3.core.Single

interface CurrencyExchangeRateRepository {
  fun fetchExchangeRate(from: Currency, to: Currency): Single<CurrencyExchangeRate>
}