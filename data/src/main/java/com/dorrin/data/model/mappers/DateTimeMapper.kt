package com.dorrin.data.model.mappers

import com.dorrin.data.model.DateTimeModel
import com.dorrin.domain.entity.DateTimeEntity

fun DateTimeEntity.toDateTimeModel(): DateTimeModel = DateTimeModel(year, month, day, hour)
fun DateTimeModel.toDateTimeEntity(): DateTimeEntity = DateTimeEntity(year, month, day, hour)