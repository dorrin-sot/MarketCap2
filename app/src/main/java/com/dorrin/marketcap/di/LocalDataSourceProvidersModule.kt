package com.dorrin.marketcap.di

import android.content.Context
import androidx.room.Room
import com.dorrin.data.source.local.Database
import com.dorrin.data.source.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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