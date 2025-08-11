package com.dorrin.domain.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.repository.AllCurrenciesRepository
import io.reactivex.rxjava3.core.Observable

class GetAllCurrenciesUseCase(private val repository: AllCurrenciesRepository) {
  operator fun invoke(): Observable<List<CurrencyEntity>> = repository.fetchAllCurrencies()
}