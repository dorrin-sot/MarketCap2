package com.dorrin.data.repository

import android.util.Log
import com.dorrin.data.entities.mappers.toCurrency
import com.dorrin.data.source.LocalDataSource
import com.dorrin.data.source.RemoteDataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.repository.AllCurrenciesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class AllCurrenciesRepositoryImpl @Inject constructor(
  private val remoteDataSource: RemoteDataSource,
  private val localDataSource: LocalDataSource,
) : AllCurrenciesRepository {
  override fun fetchAllCurrencies(): Observable<List<Currency>> =
    Observable.concat(
      // 1) Emit current local snapshot immediately
      localDataSource.getAllCurrencies()
        .subscribeOn(Schedulers.io())
        .map { it.also { Log.d(TAG, "1. Local results: $it") } }
        .toObservable(),

      // 2) Fetch remote, save, then re-query local and emit again
      remoteDataSource.getAllCurrencies()
        .subscribeOn(Schedulers.io())
        .map { it.also { Log.d(TAG, "2. Remote results: $it") } }
        .flatMapObservable { remote ->
          // wrap non-Rx insert into a Completable so we stay non-blocking
          Completable.fromAction {
            localDataSource.insertAllCurrencies(*remote.toTypedArray())
          }
            .andThen(
              localDataSource.getAllCurrencies()
                .subscribeOn(Schedulers.io())
                .map { it.also { Log.d(TAG, "3. Local results: $it") } }
                .toObservable()
            )
        }
        // If remote fails, keep the initial local emission and just stop
        .onErrorResumeNext { Observable.empty() }
    )
      .map { list -> list.map { it.toCurrency() } }
      .distinctUntilChanged()

  private companion object {
    const val TAG = "AllCurrenciesRepository"
  }
}