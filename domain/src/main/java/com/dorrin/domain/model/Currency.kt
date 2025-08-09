package com.dorrin.domain.model

data class Currency(
  val id: Long,
  val shortName: String,
  val longName: String,
) {
  override fun toString(): String {
    return "$shortName - $longName"
  }
}