package com.dorrin.domain.entity

data class DateTimeEntity(
  val year: Int,
  val month: Int = 1,
  val day: Int = 1,
  val hour: Int = 0
) {
  companion object {
    fun empty(): DateTimeEntity = DateTimeEntity(0)
  }
}