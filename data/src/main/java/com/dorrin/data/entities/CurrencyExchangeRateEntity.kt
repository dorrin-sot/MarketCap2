package com.dorrin.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index

@Entity(
  primaryKeys = ["from_id", "to_id"],
  indices = [Index("from_shortName", "to_shortName")]
)
data class CurrencyExchangeRateEntity(
  @Embedded("from_") val from: CurrencyEntity,
  @Embedded("to_") val to: CurrencyEntity,
  val rate: Float,
  @Embedded("time_") val time: DateTimeEntity
) {
  companion object {
    internal fun empty(): CurrencyExchangeRateEntity {
      return CurrencyExchangeRateEntity(
        CurrencyEntity.empty(),
        CurrencyEntity.empty(),
        0f,
        DateTimeEntity.empty(),
      )
    }
  }
}