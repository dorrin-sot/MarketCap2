package com.dorrin.data.entities

internal data class CurrencyExchangeRateEntity(
  val from: CurrencyEntity,
  val to: CurrencyEntity,
  val rate: Float,
  val time: DateTimeEntity
)