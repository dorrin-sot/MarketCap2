package com.dorrin.marketcap.di

import com.dorrin.data.repository.AllCurrenciesRepositoryImpl
import com.dorrin.data.repository.CurrencyExchangeRateRepositoryImpl
import com.dorrin.data.repository.CurrencyRepositoryImpl
import com.dorrin.domain.repository.AllCurrenciesRepository
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import com.dorrin.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryBindersModule {
  @Binds
  fun bindAllCurrenciesRepository(impl: AllCurrenciesRepositoryImpl): AllCurrenciesRepository

  @Binds
  fun bindCurrencyExchangeRateRepository(impl: CurrencyExchangeRateRepositoryImpl): CurrencyExchangeRateRepository

  @Binds
  fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
}