package com.dorrin.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import io.reactivex.rxjava3.core.Single

@Dao
internal interface LocalDataSource : DataSource {
  @Query("select * from currencyentity")
  override fun getAllCurrencies(): Single<List<CurrencyEntity>>

  @Query(
    "select * from currencyexchangerateentity " +
        "where from_shortName = :fromShortName and " +
        "to_shortName = :toShortName " +
        "limit 1"
  )
  override fun getExchangeRate(
    fromShortName: String,
    toShortName: String
  ): Single<CurrencyExchangeRateEntity>

  @Insert(onConflict = REPLACE)
  fun insertAllCurrencies(vararg currency: CurrencyEntity)

  @Insert(onConflict = REPLACE)
  fun insertExchangeRate(currencyExchangeRateEntity: CurrencyExchangeRateEntity)
}
