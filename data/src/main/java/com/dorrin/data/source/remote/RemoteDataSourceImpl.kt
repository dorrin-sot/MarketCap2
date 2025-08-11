package com.dorrin.data.source.remote

import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.source.DataSource
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteDataSourceImpl : DataSource {
  @GET("/all-currencies")
  override fun getAllCurrencies(): Single<List<CurrencyModel>>

  @GET("/convert")
  override fun getExchangeRate(
    @Query("from") fromShortName: String,
    @Query("to") toShortName: String,
  ): Single<CurrencyExchangeRateModel>
}