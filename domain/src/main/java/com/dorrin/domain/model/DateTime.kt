package com.dorrin.domain.model

data class DateTime(
  val year: Int,
  val month: Int = 1,
  val day: Int = 1,
  val hour: Int = 0
)