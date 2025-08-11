package com.dorrin.marketcap.di

import com.dorrin.data.source.remote.RemoteDataSourceImpl
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal class RemoteDataSourceProvidersModule {
  @Provides
  internal fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(
        GsonConverterFactory
          .create(
            GsonBuilder()
              .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
              .create()
          )
      )
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .build()

  @Provides
  internal fun providesRemoteDataSource(retrofit: Retrofit): RemoteDataSourceImpl =
    retrofit.create(RemoteDataSourceImpl::class.java)

  companion object {
    private const val BASE_URL = "https://market-cap-back.liara.run"
  }
}