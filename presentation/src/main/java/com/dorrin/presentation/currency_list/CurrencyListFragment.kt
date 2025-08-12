package com.dorrin.presentation.currency_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.presentation.databinding.FragmentCurrencyListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : Fragment() {
  private var _binding: FragmentCurrencyListBinding? = null
  private val binding get() = _binding!!

  private val viewModel by viewModels<CurrencyListViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    _binding = FragmentCurrencyListBinding.inflate(
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
  }

  private inner class AllCurrenciesObserver : Observer<List<CurrencyEntity>> {
    override fun onChanged(value: List<CurrencyEntity>) {
      binding.currencyList.run {
        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        adapter = CurrencyListAdapter(value.toTypedArray())
        addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() = CurrencyListFragment()
  }
}