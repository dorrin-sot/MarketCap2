package com.dorrin.data.di

import com.dorrin.data.source.DataSource
import com.dorrin.data.source.InMemoryDataSource
import com.dorrin.data.usecase.GetAllCurrenciesUseCaseImpl
import com.dorrin.data.usecase.GetCurrencyExchangeRateUseCaseImpl
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceBindersModule {
  @Binds
  fun bindDataSource(impl: InMemoryDataSource): DataSource
}