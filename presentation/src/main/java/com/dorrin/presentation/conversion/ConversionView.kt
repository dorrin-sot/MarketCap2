package com.dorrin.presentation.conversion

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.dorrin.presentation.R
import com.dorrin.presentation.R.styleable.ConversionView_rate
import com.dorrin.presentation.R.styleable.ConversionView_sourceShortName
import com.dorrin.presentation.R.styleable.ConversionView_targetShortName
import com.dorrin.presentation.databinding.ViewConversionBinding

internal class ConversionView(context: Context, attrs: AttributeSet? = null) :
  LinearLayout(context, attrs) {
  private val binding = ViewConversionBinding.inflate(LayoutInflater.from(context), this, true)

  private val sourceShortNameTextView get() = binding.sourceShortNameTextView
  private val targetShortNameTextView get() = binding.targetShortNameTextView
  private val rateTextView get() = binding.rateTextView

  var sourceShortName: String?
    get() = sourceShortNameTextView.text.toString()
    set(value) {
      sourceShortNameTextView.text = value.orEmpty()
      invalidate()
      requestLayout()
    }

  var targetShortName: String?
    get() = targetShortNameTextView.text.toString()
    set(value) {
      targetShortNameTextView.text = value.orEmpty()
      invalidate()
      requestLayout()
    }

  var rate: String?
    get() = rateTextView.text.toString()
    set(value) {
      rateTextView.text = value.orEmpty()
      invalidate()
      requestLayout()
    }

  override fun getVisibility(): Int =
    if (sourceShortName.isNullOrEmpty() || targetShortName.isNullOrEmpty() || rate.isNullOrEmpty()) GONE else VISIBLE

  init {
    context.theme.obtainStyledAttributes(
      attrs,
      R.styleable.ConversionView,
      0,
      0
    ).apply {

      try {
        sourceShortName = getString(ConversionView_sourceShortName) ?: ""
        targetShortName = getString(ConversionView_targetShortName) ?: ""
        rate = getString(ConversionView_rate) ?: ""
      } finally {
        recycle()
      }
    }
  }
}