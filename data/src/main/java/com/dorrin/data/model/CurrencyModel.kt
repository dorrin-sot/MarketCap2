package com.dorrin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyModel(
  @PrimaryKey val shortName: String,
  val longName: String,
) {
  companion object {
    internal fun empty(): CurrencyModel = CurrencyModel("empty", "empty")
  }
}