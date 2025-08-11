package com.dorrin.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.CurrencyModel

@Database(
  entities = [CurrencyModel::class, CurrencyExchangeRateModel::class],
  version = 2
)
abstract class Database : RoomDatabase() {
  abstract fun localDataSource(): LocalDataSourceImpl
}