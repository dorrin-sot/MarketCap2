package com.dorrin.domain.model

data class CurrencyExchangeRate(
  val from: Currency,
  val to: Currency,
  val rate: Float,
  val time: DateTime
) {
  fun isEmpty(): Boolean = this == empty()

  companion object {
    fun empty(): CurrencyExchangeRate =
      CurrencyExchangeRate(
        Currency.empty(),
        Currency.empty(),
        0f,
        DateTime.empty(),
      )
  }
}