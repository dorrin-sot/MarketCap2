package com.dorrin.data.usecase

import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.repository.AllCurrenciesRepository
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

// todo move usecase IMPL to domain
internal class GetAllCurrenciesUseCaseImpl @Inject constructor(
  private val repository: AllCurrenciesRepository,
) : GetAllCurrenciesUseCase {
  override fun invoke(): Observable<List<CurrencyEntity>> = repository.fetchAllCurrencies()
}