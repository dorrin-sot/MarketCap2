package com.dorrin.domain.model

data class Currency(
  val id: Long,
  val shortName: String,
  val longName: String,
  val country: Country,
) {
  override fun toString(): String {
    return "${country.flag} $shortName - $longName"
  }
}