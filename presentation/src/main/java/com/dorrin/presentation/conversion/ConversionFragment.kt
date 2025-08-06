package com.dorrin.presentation.conversion

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dorrin.domain.model.Currency
import com.dorrin.presentation.databinding.FragmentConversionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversionFragment : Fragment() {
  private var _binding: FragmentConversionBinding? = null
  private val binding get() = _binding!!

  private val viewModel by viewModels<ConversionViewModel>()

  val allCurrenciesObserver = object : Observer<List<Currency>> {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onChanged(value: List<Currency>) {
      val context = requireContext()
      _binding?.sourceCurrencySelector?.onItemSelectedListener = SourceOnItemSelectedListener()
      _binding?.targetCurrencySelector?.onItemSelectedListener = TargetOnItemSelectedListener()

      arrayOf(_binding?.sourceCurrencySelector, _binding?.targetCurrencySelector)
        .filterNotNull()
        .forEach { atv ->
          atv.setAdapter(CurrencyAdapter(context, value))
          atv.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) atv.showDropDown()
            else atv.dismissDropDown()
          }
          atv.threshold = 1
        }
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
    viewModel.allCurrencies.observeForever(allCurrenciesObserver)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding?.unbind()
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