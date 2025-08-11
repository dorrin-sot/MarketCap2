package com.dorrin.marketcap.di

import com.dorrin.domain.repository.AllCurrenciesRepository
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class UseCaseProvidersModule {
  @Provides
  fun provideGetAllCurrenciesUseCase(repository: AllCurrenciesRepository): GetAllCurrenciesUseCase =
    GetAllCurrenciesUseCase(repository)

  @Provides
  fun provideGetCurrencyExchangeRateUseCase(repository: CurrencyExchangeRateRepository): GetCurrencyExchangeRateUseCase =
    GetCurrencyExchangeRateUseCase(repository)
}