package com.dorrin.data.source

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RemoteDataSource : DataSource {
  @GET("/all-currencies")
  override fun getAllCurrencies(): Single<List<CurrencyEntity>>

  @GET("/convert")
  override fun getExchangeRate(
    @Query("from") fromShortName: String,
    @Query("to") toShortName: String
  ): Single<CurrencyExchangeRateEntity>
}