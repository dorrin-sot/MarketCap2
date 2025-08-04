package com.dorrin.domain.model

import java.time.LocalDateTime

data class CurrencyExchangeRate(
  val from: Money,
  val to: Money,
  val rate: Float,
  val time: LocalDateTime
)