package com.dorrin.presentation.conversion

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dorrin.domain.model.Currency
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.FragmentConversionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversionFragment : Fragment() {
  private var _binding: FragmentConversionBinding? = null
  private val binding get() = _binding!!

  private val viewModel by viewModels<ConversionViewModel>()

  val allCurrenciesObserver = object : Observer<List<Currency>> {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onChanged(value: List<Currency>) =
      arrayOf(
        _binding?.sourceCurrencySelector,
        _binding?.targetCurrencySelector
      ).filterNotNull()
        .forEach { atv: AutoCompleteTextView ->
          atv.setAdapter(CurrencyAdapter(requireContext(), value))

          atv.onItemClickListener = when (atv.id) {
            R.id.sourceCurrencySelector -> SourceOnItemSelectedListener()
            R.id.targetCurrencySelector -> TargetOnItemSelectedListener()
            else -> throw IllegalAccessException()
          }

          atv.threshold = 1 // values of less than 0 don't work
        }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentConversionBinding.inflate(
      inflater,
      container,
      false
    )

    binding.viewModel = viewModel
    binding.lifecycleOwner = viewLifecycleOwner

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.allCurrencies.observeForever(allCurrenciesObserver)
  }

  override fun onStart() {
    super.onStart()
    viewModel.fetchAllCurrencies()
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.allCurrencies.removeObserver(allCurrenciesObserver)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding?.unbind()
    _binding = null
  }

  inner class SourceOnItemSelectedListener : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectSourceCurrency(id)
  }

  inner class TargetOnItemSelectedListener : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectTargetCurrency(id)
  }

  companion object {
    @JvmStatic
    fun newInstance() = ConversionFragment()
  }
}