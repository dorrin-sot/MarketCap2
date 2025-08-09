package com.dorrin.data.entities.mappers

import com.dorrin.data.entities.CurrencyEntity
import com.dorrin.domain.model.Currency

fun Currency.toCurrencyEntity(): CurrencyEntity = CurrencyEntity(id, shortName, longName)

fun CurrencyEntity.toCurrency(): Currency = Currency(id, shortName, longName)