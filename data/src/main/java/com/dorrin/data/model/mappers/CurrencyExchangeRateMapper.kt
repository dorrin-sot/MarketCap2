package com.dorrin.data.model.mappers

import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.domain.entity.CurrencyExchangeRateEntity


fun CurrencyExchangeRateEntity.toCurrencyExchangeRateModel(): CurrencyExchangeRateModel =
  CurrencyExchangeRateModel(
    from.toCurrencyModel(),
    to.toCurrencyModel(),
    rate,
    time.toDateTimeModel()
  )

fun CurrencyExchangeRateModel.toCurrencyExchangeRateEntity(): CurrencyExchangeRateEntity =
  CurrencyExchangeRateEntity(
    from.toCurrencyEntity(),
    to.toCurrencyEntity(),
    rate,
    time.toDateTimeEntity()
  )