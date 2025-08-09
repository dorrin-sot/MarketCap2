package com.dorrin.data.source

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import io.reactivex.rxjava3.core.Single

internal interface DataSource {
  fun getAllCurrencies(): Single<List<CurrencyEntity>>

  fun getExchangeRate(
    fromShortName: String,
    toShortName: String
  ): Single<CurrencyExchangeRateEntity>
}