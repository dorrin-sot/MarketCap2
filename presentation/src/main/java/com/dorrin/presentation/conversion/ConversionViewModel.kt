package com.dorrin.presentation.conversion

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModel
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase

internal class ConversionViewModel(
  val presenter: ConversionPresenter,
  private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
  private val currencyExchangeRateUseCase: GetCurrencyExchangeRateUseCase,
) : ViewModel(), ConversionController {
  val conversionDisplayVisibility get() = if (presenter.showConversionDisplay) VISIBLE else GONE

  override fun fetchAllCurrencies() {
    presenter.allCurrencies = getAllCurrenciesUseCase()
  }

  override fun selectSourceCurrency(id: Long) {
    presenter.sourceCurrency = presenter.allCurrencies.first { it.id == id }
    performConversion()
  }

  override fun selectTargetCurrency(id: Long) {
    presenter.targetCurrency = presenter.allCurrencies.first { it.id == id }
    performConversion()
  }

  override fun performConversion() {
    val from = presenter.sourceCurrency ?: return
    val to = presenter.targetCurrency ?: return
    presenter.conversion = currencyExchangeRateUseCase(from, to)
  }
}