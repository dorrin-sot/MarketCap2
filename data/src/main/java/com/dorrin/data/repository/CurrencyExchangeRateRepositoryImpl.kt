package com.dorrin.data.repository

import android.util.Log
import com.dorrin.data.model.CurrencyExchangeRateModel
import com.dorrin.data.model.mappers.toCurrencyExchangeRateEntity
import com.dorrin.data.source.local.LocalDataSourceImpl
import com.dorrin.data.source.remote.RemoteDataSourceImpl
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.domain.entity.CurrencyExchangeRateEntity
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class CurrencyExchangeRateRepositoryImpl @Inject constructor(
  private val remoteDataSourceImpl: RemoteDataSourceImpl,
  private val localDataSourceImpl: LocalDataSourceImpl,
) : CurrencyExchangeRateRepository {
  override fun fetchExchangeRate(
    from: CurrencyEntity,
    to: CurrencyEntity,
  ): Observable<CurrencyExchangeRateEntity> {
    val fromShortName = from.shortName
    val toShortName = to.shortName

    return Observable.concat(
      // 1) Emit current local snapshot immediately
      localDataSourceImpl.getExchangeRate(fromShortName, toShortName)
        .subscribeOn(Schedulers.io())
        .onErrorReturn { CurrencyExchangeRateModel.empty() }
        .map { it.also { Log.d(TAG, "1. Local results: $it") } }
        .toObservable(),

      // 2) Fetch remote, save, then re-query local and emit again
      remoteDataSourceImpl.getExchangeRate(fromShortName, toShortName)
        .subscribeOn(Schedulers.io())
        .map { it.also { Log.d(TAG, "2. Remote results: $it") } }
        .flatMapObservable { remote ->
          // wrap non-Rx insert into a Completable so we stay non-blocking
          Completable.fromAction {
            localDataSourceImpl.insertExchangeRate(remote)
          }
            .andThen(
              localDataSourceImpl.getExchangeRate(fromShortName, toShortName)
                .onErrorReturn { CurrencyExchangeRateModel.empty() }
                .subscribeOn(Schedulers.io())
                .map { it.also { Log.d(TAG, "3. Local results: $it") } }
                .toObservable()
            )
        }
        // If remote fails, keep the initial local emission and just stop
        .onErrorResumeNext { Observable.empty() }
    )
      .map { it.toCurrencyExchangeRateEntity() }
      .distinctUntilChanged()
  }

  private companion object {
    const val TAG = "CurrencyExchangeRateRepository"
  }
}