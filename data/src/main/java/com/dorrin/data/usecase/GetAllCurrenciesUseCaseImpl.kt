package com.dorrin.data.usecase

import com.dorrin.domain.model.Currency
import com.dorrin.domain.repository.AllCurrenciesRepository
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetAllCurrenciesUseCaseImpl @Inject constructor(
  private val repository: AllCurrenciesRepository
) : GetAllCurrenciesUseCase {
  override fun invoke(): Single<List<Currency>> = repository.fetchAllCurrencies()
}