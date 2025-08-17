package com.dorrin.presentation.currency_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dorrin.domain.entity.CurrencyEntity
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.FragmentCurrencyListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyListFragment : Fragment(), MenuProvider {
  private var _binding: FragmentCurrencyListBinding? = null
  private val binding get() = _binding!!

  private lateinit var menu: Menu

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
    requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.allCurrencies.observe(viewLifecycleOwner, AllCurrenciesObserver())
    viewModel.filteredCurrencies.observe(viewLifecycleOwner, FilteredCurrenciesObserver())
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    this.menu = menu
    menuInflater.inflate(R.menu.currency_list_toolbar_menu, menu)

    (menu.findItem(R.id.toolbar_search).actionView as? SearchView)?.let { searchView ->
      searchView.setQuery(viewModel.currencyQuery.value, false)
      searchView.setOnQueryTextListener(SearchQueryListener())
    }

  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

  private inner class AllCurrenciesObserver : Observer<List<CurrencyEntity>> {
    override fun onChanged(value: List<CurrencyEntity>) {
      binding.currencyRecyclerView.run {
        val linearLayoutManager = LinearLayoutManager(context)
        layoutManager = linearLayoutManager
        adapter = CurrencyListAdapter(value.toTypedArray())
        addItemDecoration(DividerItemDecoration(context, linearLayoutManager.orientation))
      }
    }
  }

  private inner class FilteredCurrenciesObserver : Observer<List<CurrencyEntity>> {
    override fun onChanged(value: List<CurrencyEntity>) {
      (binding.currencyRecyclerView.adapter as? CurrencyListAdapter)
        ?.filter(viewModel.currencyQuery.value, value)
    }
  }

  private inner class SearchQueryListener : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean = search(query)

    override fun onQueryTextChange(newText: String?): Boolean = search(newText)

    private fun search(query: String?): Boolean = viewModel.search(query?.trim()).let { true }
  }
}