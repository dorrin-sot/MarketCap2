package com.dorrin.domain.entity

data class DateTimeEntity(
  val year: Int,
  val month: Int = 1,
  val day: Int = 1,
  val hour: Int = 0,
) {
  override fun toString(): String =
    "%04d-%02d-%02d %02d:00%s"
      .format(
        year, month, day,
        hour % 12,
        if (hour < 12) "AM" else "PM"
      )

  companion object {
    fun empty(): DateTimeEntity = DateTimeEntity(0)
  }
}