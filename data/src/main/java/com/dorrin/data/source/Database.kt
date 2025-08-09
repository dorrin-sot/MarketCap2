package com.dorrin.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity

@Database(
  entities = [CurrencyEntity::class, CurrencyExchangeRateEntity::class],
  version = 1
)
internal abstract class Database : RoomDatabase() {
  abstract fun localDataSource(): LocalDataSource
}
