package com.dorrin.marketcap.di

import com.dorrin.data.repository.AllCurrenciesRepositoryImpl
import com.dorrin.data.repository.CurrencyExchangeRateRepositoryImpl
import com.dorrin.data.repository.CurrencyRepositoryImpl
import com.dorrin.data.source.local.LocalDataSourceImpl
import com.dorrin.data.source.remote.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class RepositoryImplProvidersModule {
  @Provides
  fun provideAllCurrenciesRepositoryImpl(
    remote: RemoteDataSourceImpl,
    local: LocalDataSourceImpl,
  ): AllCurrenciesRepositoryImpl = AllCurrenciesRepositoryImpl(remote, local)

  @Provides
  fun provideCurrencyExchangeRateRepositoryImpl(
    remote: RemoteDataSourceImpl,
    local: LocalDataSourceImpl,
  ): CurrencyExchangeRateRepositoryImpl = CurrencyExchangeRateRepositoryImpl(remote, local)

  @Provides
  fun provideCurrencyRepositoryImpl(
    remote: RemoteDataSourceImpl,
    local: LocalDataSourceImpl,
  ): CurrencyRepositoryImpl = CurrencyRepositoryImpl(remote, local)
}