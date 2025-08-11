package com.dorrin.data.di

import android.content.Context
import androidx.room.Room
import com.dorrin.data.source.local.Database
import com.dorrin.data.source.local.LocalDataSourceImpl
import com.dorrin.data.source.remote.RemoteDataSourceImpl
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
internal class LocalDataSourceProvidersModule {
  @Singleton
  @Provides
  internal fun providesDatabase(@ApplicationContext applicationContext: Context): Database =
    Room.databaseBuilder(applicationContext, Database::class.java, DB_NAME)
      .fallbackToDestructiveMigration(true)
      .build()

  @Singleton
  @Provides
  internal fun providesLocalDataSource(database: Database): LocalDataSourceImpl =
    database.localDataSource()

  companion object {
    private const val DB_NAME = "database"
  }
}