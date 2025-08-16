package com.dorrin.presentation.conversion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    arguments?.let {
      val args = ConversionFragmentArgs.fromBundle(it)
      Log.d("ConversionFragment", "args: $args")

      val fromId = args.fromId?.toLong()
      val fromShortName = args.fromShortName
      val fromLongName = args.fromLongName

      if (fromId != null) {
        if (fromShortName != null && fromLongName != null)
          viewModel.selectSourceCurrency(CurrencyEntity(fromId, fromShortName, fromLongName))
        else
          viewModel.selectSourceCurrency(fromId)
      }

      val toId = args.toId?.toLong()
      val toShortName = args.toShortName
      val toLongName = args.toLongName

      if (toId != null) {
        if (toShortName != null && toLongName != null)
          viewModel.selectTargetCurrency(CurrencyEntity(toId, toShortName, toLongName))
        else
          viewModel.selectTargetCurrency(toId)
      }
    }
  }

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
    viewModel.sourceCurrency.observe(
      viewLifecycleOwner,
      CurrencyObserver(binding.sourceCurrencySelector)
    )
    viewModel.targetCurrency.observe(
      viewLifecycleOwner,
      CurrencyObserver(binding.targetCurrencySelector)
    )
    binding.swapCurrenciesButton.setOnClickListener { viewModel.swapCurrencies() }
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
            R.id.source_currency_selector -> SourceOnItemSelectedListener()
            R.id.target_currency_selector -> TargetOnItemSelectedListener()
            else -> throw IllegalAccessException()
          }

          atv.threshold = 1 // values of less than 0 don't work
        }
    }
  }

  private inner class CurrencyObserver(
    private val selector: AutoCompleteTextView,
  ) : Observer<CurrencyEntity?> {
    override fun onChanged(value: CurrencyEntity?) =
      selector.setText(value?.toString() ?: "", false)
  }
}