package com.dorrin.data.model

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["from_shortName", "to_shortName"])
data class CurrencyExchangeRateModel(
  @Embedded("from_") val from: CurrencyModel,
  @Embedded("to_") val to: CurrencyModel,
  val rate: Float,
  @Embedded("time_") val time: DateTimeModel,
) {
  companion object {
    internal fun empty(): CurrencyExchangeRateModel {
      return CurrencyExchangeRateModel(
        CurrencyModel.empty(),
        CurrencyModel.empty(),
        0f,
        DateTimeModel.empty(),
      )
    }
  }
}