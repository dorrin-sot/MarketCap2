package com.dorrin.data.repository

import android.util.Log
import com.dorrin.data.model.CurrencyModel
import com.dorrin.data.model.mappers.toCurrencyEntity
import com.dorrin.data.source.local.LocalDataSourceImpl
import com.dorrin.data.source.remote.RemoteDataSourceImpl
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.repository.CurrencyRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CurrencyRepositoryImpl(
  val remoteDataSourceImpl: RemoteDataSourceImpl,
  val localDataSourceImpl: LocalDataSourceImpl,
) : CurrencyRepository {
  override fun fetchCurrency(shortName: String): Observable<CurrencyEntity> {
    return Observable.concat(
      // 1) Emit current local snapshot immediately
      localDataSourceImpl.getCurrency(shortName)
        .subscribeOn(Schedulers.io())
        .onErrorReturn { CurrencyModel.empty() }
        .map { it.also { Log.d(TAG, "1. Local results: $it") } }
        .toObservable(),

      // 2) Fetch remote, save, then re-query local and emit again
      remoteDataSourceImpl.getCurrency(shortName)
        .subscribeOn(Schedulers.io())
        .map { it.also { Log.d(TAG, "2. Remote results: $it") } }
        .flatMapObservable { remote ->
          // wrap non-Rx insert into a Completable so we stay non-blocking
          Completable.fromAction {
            localDataSourceImpl.insertCurrency(remote)
          }
            .andThen(
              localDataSourceImpl.getCurrency(shortName)
                .onErrorReturn { CurrencyModel.empty() }
                .subscribeOn(Schedulers.io())
                .map { it.also { Log.d(TAG, "3. Local results: $it") } }
                .toObservable()
            )
        }
        // If remote fails, keep the initial local emission and just stop
        .onErrorResumeNext { Observable.empty() }
    )
      .map { it.toCurrencyEntity() }
      .distinctUntilChanged()
  }

  private companion object {
    const val TAG = "CurrencyRepository"
  }
}