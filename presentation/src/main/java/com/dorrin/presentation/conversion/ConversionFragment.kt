package com.dorrin.presentation.conversion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.FragmentConversionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversionFragment : Fragment() {
  private var _binding: FragmentConversionBinding? = null
  private val binding get() = _binding!!

  private val viewModel by viewModels<ConversionViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.allCurrencies.observe(viewLifecycleOwner, AllCurrenciesObserver())
    binding.swapCurrenciesButton.setOnClickListener(SwapCurrenciesButtonOnClickListener())
  }

  override fun onStart() {
    super.onStart()
    viewModel.fetchAllCurrencies() // todo move to viewModel::init
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding?.unbind()
    _binding = null
  }

  private inner class SourceOnItemSelectedListener : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectSourceCurrency(id)
  }

  private inner class TargetOnItemSelectedListener : AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
      viewModel.selectTargetCurrency(id)
  }

  private inner class AllCurrenciesObserver : Observer<List<CurrencyEntity>> {
    override fun onChanged(value: List<CurrencyEntity>) {
      arrayOf(binding.sourceCurrencySelector, binding.targetCurrencySelector)
        .forEach { atv ->
          atv.setAdapter(CurrencyAdapter(requireContext(), value))

          atv.onItemClickListener = when (atv.id) {
            R.id.sourceCurrencySelector -> SourceOnItemSelectedListener()
            R.id.targetCurrencySelector -> TargetOnItemSelectedListener()
            else -> throw IllegalAccessException()
          }

          atv.threshold = 1 // values of less than 0 don't work
        }
    }
  }

  private inner class SwapCurrenciesButtonOnClickListener : View.OnClickListener {
    override fun onClick(v: View?) {
      viewModel.swapCurrencies()

      mapOf(
        viewModel.sourceCurrency to binding.sourceCurrencySelector,
        viewModel.targetCurrency to binding.targetCurrencySelector,
      ).forEach { liveData, selector ->
        liveData.value.let {
          selector.run {
            if (it == null) setText("", false)
            else setText(it.toString(), false)
            dismissDropDown()
          }
        }
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() = ConversionFragment()
  }
}