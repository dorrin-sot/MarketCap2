package com.dorrin.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyEntity(
  @PrimaryKey val id: Long,
  val shortName: String,
  val longName: String,
)