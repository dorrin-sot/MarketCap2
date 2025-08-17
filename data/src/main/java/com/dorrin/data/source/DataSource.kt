package com.dorrin.data.source

import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.CurrencyModel
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface DataSource {
  fun getAllCurrencies(): Single<List<CurrencyModel>>

  fun getExchangeRate(
    fromShortName: String,
    toShortName: String,
  ): Single<CurrencyExchangeRateModel>

  fun getCurrency(shortName: String): Maybe<CurrencyModel>
}