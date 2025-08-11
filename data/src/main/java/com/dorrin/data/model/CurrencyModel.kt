package com.dorrin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyModel(
  @PrimaryKey val id: Long,
  val shortName: String,
  val longName: String,
) {
  companion object {
    internal fun empty(): CurrencyModel = CurrencyModel(0, "empty", "empty")
  }
}