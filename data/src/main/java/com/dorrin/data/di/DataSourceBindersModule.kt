package com.dorrin.data.di

import com.dorrin.data.source.DataSource
import com.dorrin.data.source.InMemoryDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceBindersModule {
  @Binds
  fun bindDataSource(impl: InMemoryDataSourceImpl): DataSource
}