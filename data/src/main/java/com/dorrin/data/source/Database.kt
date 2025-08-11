package com.dorrin.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.model.CurrencyExchangeRateModel

@Database(
  entities = [CurrencyModel::class, CurrencyExchangeRateModel::class],
  version = 1
)
internal abstract class Database : RoomDatabase() {
  abstract fun localDataSource(): LocalDataSource
}
