package com.dorrin.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.dorrin.data.repository.AllCurrenciesRepository
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.mappers.toCurrency

class GetAllCurrenciesUseCase(
  private val allCurrenciesRepository: AllCurrenciesRepository
) {
  operator fun invoke(): LiveData<List<Currency>> = allCurrenciesRepository.getAllCurrencies()
    .map { it.map { entity -> entity.toCurrency() } }
}