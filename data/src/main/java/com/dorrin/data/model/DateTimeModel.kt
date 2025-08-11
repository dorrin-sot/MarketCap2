package com.dorrin.data.model

data class DateTimeModel(
  val year: Int,
  val month: Int = 1,
  val day: Int = 1,
  val hour: Int = 0
) {
  companion object {
    fun empty(): DateTimeModel = DateTimeModel(0)
  }
}