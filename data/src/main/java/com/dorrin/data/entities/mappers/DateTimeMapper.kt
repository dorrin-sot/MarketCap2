package com.dorrin.data.entities.mappers

import com.dorrin.data.entities.DateTimeEntity
import com.dorrin.domain.model.DateTime

fun DateTime.toDateTimeEntity(): DateTimeEntity = DateTimeEntity(year, month, day, hour)
fun DateTimeEntity.toDateTime(): DateTime = DateTime(year, month, day, hour)