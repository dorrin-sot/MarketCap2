package com.dorrin.data.model.mappers

import com.dorrin.data.model.CurrencyModel
import com.dorrin.domain.entity.CurrencyEntity

fun CurrencyEntity.toCurrencyModel(): CurrencyModel = CurrencyModel(id, shortName, longName)

fun CurrencyModel.toCurrencyEntity(): CurrencyEntity = CurrencyEntity(id, shortName, longName)