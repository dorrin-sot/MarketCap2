package com.dorrin.data.entities.mappers

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.domain.model.Currency

fun Currency.toCurrencyEntity(): CurrencyEntity = CurrencyEntity(shortName, longName, country)

fun CurrencyEntity.toCurrency(): Currency = Currency(shortName, longName, country)