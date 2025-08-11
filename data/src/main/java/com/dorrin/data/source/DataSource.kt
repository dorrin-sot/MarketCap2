package com.dorrin.data.source

import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.model.CurrencyExchangeRateModel
import io.reactivex.rxjava3.core.Single

internal interface DataSource {
  fun getAllCurrencies(): Single<List<CurrencyModel>>

  fun getExchangeRate(
    fromShortName: String,
    toShortName: String
  ): Single<CurrencyExchangeRateModel>
}