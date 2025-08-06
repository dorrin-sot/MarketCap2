package com.dorrin.data.repository

import com.dorrin.data.entities.mappers.toCurrency
import com.dorrin.data.source.DataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.repository.AllCurrenciesRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class AllCurrenciesRepositoryImpl @Inject constructor(
  private val dataSource: DataSource
) : AllCurrenciesRepository {
  override fun fetchAllCurrencies(): Single<List<Currency>> =
    dataSource.getAllCurrencies().map { entities -> entities.map { entity -> entity.toCurrency() }.also { println(entities) } }
}