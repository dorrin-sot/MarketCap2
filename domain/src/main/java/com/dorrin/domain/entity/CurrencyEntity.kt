package com.dorrin.domain.entity

data class CurrencyEntity(
  val shortName: String,
  val longName: String,
) {
  override fun toString(): String {
    return "$shortName - $longName"
  }

  companion object {
    fun empty(): CurrencyEntity = CurrencyEntity("empty", "empty")
  }
}