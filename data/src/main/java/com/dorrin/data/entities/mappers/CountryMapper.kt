package com.dorrin.data.entities.mappers

import com.dorrin.data.entities.CountryEntity
import com.dorrin.domain.model.Country

fun Country.toCountryEntity(): CountryEntity = CountryEntity(shortName, longName, flag)

fun CountryEntity.toCountry(): Country = Country(shortName, longName, flag)