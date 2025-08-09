package com.dorrin.data.repository

import android.util.Log
import com.dorrin.data.entities.CurrencyExchangeRateEntity
import com.dorrin.data.entities.mappers.toCurrencyExchangeRate
import com.dorrin.data.source.LocalDataSource
import com.dorrin.data.source.RemoteDataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.repository.CurrencyExchangeRateRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class CurrencyExchangeRateRepositoryImpl @Inject constructor(
  private val remoteDataSource: RemoteDataSource,
  private val localDataSource: LocalDataSource,
) : CurrencyExchangeRateRepository {
  override fun fetchExchangeRate(from: Currency, to: Currency): Observable<CurrencyExchangeRate> {
    val fromShortName = from.shortName
    val toShortName = to.shortName

    return Observable.concat(
      // 1) Emit current local snapshot immediately
      localDataSource.getExchangeRate(fromShortName, toShortName)
        .subscribeOn(Schedulers.io())
        .onErrorReturn { CurrencyExchangeRateEntity.empty() }
        .map { it.also { Log.d(TAG, "1. Local results: $it") } }
        .toObservable(),

      // 2) Fetch remote, save, then re-query local and emit again
      remoteDataSource.getExchangeRate(fromShortName, toShortName)
        .subscribeOn(Schedulers.io())
        .map { it.also { Log.d(TAG, "2. Remote results: $it") } }
        .flatMapObservable { remote ->
          // wrap non-Rx insert into a Completable so we stay non-blocking
          Completable.fromAction {
            localDataSource.insertExchangeRate(remote)
          }
            .andThen(
              localDataSource.getExchangeRate(fromShortName, toShortName)
                .onErrorReturn { CurrencyExchangeRateEntity.empty() }
                .subscribeOn(Schedulers.io())
                .map { it.also { Log.d(TAG, "3. Local results: $it") } }
                .toObservable()
            )
        }
        // If remote fails, keep the initial local emission and just stop
        .onErrorResumeNext { Observable.empty() }
    )
      .map { it.toCurrencyExchangeRate() }
      .distinctUntilChanged()
  }

  private companion object {
    const val TAG = "CurrencyExchangeRateRepository"
  }
}