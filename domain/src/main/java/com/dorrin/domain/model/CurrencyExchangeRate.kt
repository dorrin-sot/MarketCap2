package com.dorrin.domain.model

data class CurrencyExchangeRate(
  val from: Currency,
  val to: Currency,
  val rate: Float,
  val time: DateTime
)