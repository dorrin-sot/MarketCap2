package com.dorrin.domain.entity

data class CurrencyEntity(
  val id: Long,
  val shortName: String,
  val longName: String,
) {
  override fun toString(): String {
    return "$shortName - $longName"
  }

  companion object {
    fun empty(): CurrencyEntity = CurrencyEntity(0, "empty", "empty")
  }
}