package com.dorrin.presentation.conversion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.dorrin.data.entities.mappers.toCurrency
import com.dorrin.data.entities.mappers.toCurrencyEntity
import com.dorrin.data.entities.mappers.toCurrencyExchangeRate
import com.dorrin.data.source.InMemoryDataSource
import com.dorrin.domain.model.Currency
import com.dorrin.domain.model.CurrencyExchangeRate
import com.dorrin.domain.usecase.GetAllCurrenciesUseCase
import com.dorrin.domain.usecase.GetCurrencyExchangeRateUseCase
import com.dorrin.presentation.databinding.FragmentConversionBinding

class ConversionFragment : Fragment() {
  private var _binding: FragmentConversionBinding? = null
  private val binding get() = _binding!!

  private lateinit var viewModel: ConversionViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val inMemorySource = InMemoryDataSource()

    viewModel = ConversionViewModel(
      ConversionPresenter(),
      object : GetAllCurrenciesUseCase {
        override fun invoke(): List<Currency> =
          inMemorySource.getAllCurrencies().map { it.toCurrency() }
      },
      object : GetCurrencyExchangeRateUseCase {
        override fun invoke(from: Currency, to: Currency): CurrencyExchangeRate =
          inMemorySource.getExchangeRate(from.toCurrencyEntity(), to.toCurrencyEntity())
            .toCurrencyExchangeRate()
      }
    )

    _binding = FragmentConversionBinding.inflate(
      inflater,
      container,
      false
    )

    return binding.root
  }

  override fun onStart() {
    super.onStart()

    viewModel.fetchAllCurrencies()
    val allCurrencies = viewModel.presenter.allCurrencies

    val context = requireContext()
    binding.sourceCurrencySelector.run {
      setAdapter(CurrencyAdapter(context, allCurrencies))
      onItemSelectedListener = SourceOnItemSelectedListener()
    }
    binding.targetCurrencySelector.run {
      setAdapter(CurrencyAdapter(context, allCurrencies))
      onItemSelectedListener = TargetOnItemSelectedListener()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  inner class SourceOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectSourceCurrency(id)

    override fun onNothingSelected(parent: AdapterView<*>?) =
      viewModel.selectSourceCurrency(-1)
  }

  inner class TargetOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectTargetCurrency(id)

    override fun onNothingSelected(parent: AdapterView<*>?) =
      viewModel.selectTargetCurrency(-1)
  }

  companion object {
    @JvmStatic
    fun newInstance() = ConversionFragment()
  }
}