package com.dorrin.data.di

import android.content.Context
import androidx.room.Room
import com.dorrin.data.source.Database
import com.dorrin.data.source.LocalDataSourceImpl
import com.dorrin.data.source.RemoteDataSourceImpl
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DataSourceProvidersModule {
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


  @Singleton
  @Provides
  internal fun providesDatabase(@ApplicationContext applicationContext: Context): Database =
    Room.databaseBuilder(applicationContext, Database::class.java, DB_NAME)
      .build()

  @Singleton
  @Provides
  internal fun providesLocalDataSource(database: Database): LocalDataSourceImpl =
    database.localDataSource()

  companion object {
    private const val BASE_URL = "https://market-cap-back.liara.run"
    private const val DB_NAME = "database"
  }
}