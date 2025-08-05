package com.dorrin.domain.repository

import com.dorrin.domain.model.Currency
import io.reactivex.rxjava3.core.Single

interface AllCurrenciesRepository {
  fun fetchAllCurrencies(): Single<List<Currency>>
}