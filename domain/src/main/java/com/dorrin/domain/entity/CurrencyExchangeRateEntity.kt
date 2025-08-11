package com.dorrin.domain.entity

data class CurrencyExchangeRateEntity(
  val from: CurrencyEntity,
  val to: CurrencyEntity,
  val rate: Float,
  val time: DateTimeEntity,
) {
  fun isEmpty(): Boolean = this == empty()

  companion object {
    fun empty(): CurrencyExchangeRateEntity =
      CurrencyExchangeRateEntity(
        CurrencyEntity.empty(),
        CurrencyEntity.empty(),
        0f,
        DateTimeEntity.empty(),
      )
  }
}