package com.dorrin.presentation.currency_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    this.menu = menu
    menuInflater.inflate(R.menu.currency_list_toolbar_menu, menu)
    onMenuItemSelected(menu.findItem(R.id.toolbar_search_off)) // search off by default
  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
    when (menuItem.itemId) {
      R.id.toolbar_search -> {
        updateSearchBarVisibility(true)
        true
      }

      R.id.toolbar_search_off -> {
        updateSearchBarVisibility(false)
        true
      }

      else -> false
    }


  private fun updateSearchBarVisibility(visible: Boolean) {
    menu.findItem(R.id.toolbar_search).isVisible = !visible
    menu.findItem(R.id.toolbar_search_off).isVisible = visible
    // todo show and hide search bar
  }

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
}