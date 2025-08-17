package com.dorrin.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.source.DataSource
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface LocalDataSourceImpl : DataSource {
  @Query("select * from CurrencyModel")
  override fun getAllCurrencies(): Single<List<CurrencyModel>>

  @Query(
    "select * from CurrencyExchangeRateModel " +
        "where from_shortName = :fromShortName and " +
        "to_shortName = :toShortName " +
        "limit 1"
  )
  override fun getExchangeRate(
    fromShortName: String,
    toShortName: String,
  ): Single<CurrencyExchangeRateModel>

  @Query(
    "select * from CurrencyModel " +
        "where shortName = :shortName " +
        "limit 1"
  )
  override fun getCurrency(shortName: String): Maybe<CurrencyModel>

  @Insert(onConflict = REPLACE)
  fun insertAllCurrencies(vararg currency: CurrencyModel)

  @Insert(onConflict = REPLACE)
  fun insertExchangeRate(currencyExchangeRateEntity: CurrencyExchangeRateModel)

  @Insert(onConflict = REPLACE)
  fun insertCurrency(currency: CurrencyModel)
}
