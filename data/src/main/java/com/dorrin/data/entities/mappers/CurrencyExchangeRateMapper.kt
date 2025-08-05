package com.dorrin.data.entities.mappers

import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.domain.model.CurrencyExchangeRate


fun CurrencyExchangeRate.toCurrencyExchangeRateEntity(): CurrencyExchangeRateEntity =
  CurrencyExchangeRateEntity(
    from.toCurrencyEntity(),
    to.toCurrencyEntity(),
    rate,
    time.toDateTimeEntity()
  )

fun CurrencyExchangeRateEntity.toCurrencyExchangeRate(): CurrencyExchangeRate =
  CurrencyExchangeRate(
    from.toCurrency(),
    to.toCurrency(),
    rate,
    time.toDateTime()
  )