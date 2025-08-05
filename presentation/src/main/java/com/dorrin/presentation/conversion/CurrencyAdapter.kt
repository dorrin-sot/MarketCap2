package com.dorrin.presentation.conversion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dorrin.domain.model.Currency
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.HolderDropdownCurrencyItemBinding

internal class CurrencyAdapter(
  context: Context,
  currencies: List<Currency>
) : ArrayAdapter<Currency>(
  context,
  R.layout.holder_dropdown_currency_item,
  currencies,
) {
  override fun getItemId(position: Int): Long = getItem(position)?.id ?: 0L

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    return createBindingView(position, convertView, parent)
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    return createBindingView(position, convertView, parent)
  }

  private fun createBindingView(position: Int, convertView: View?, parent: ViewGroup): View {
    val inflater = LayoutInflater.from(context)

    val binding = if (convertView == null) {
      HolderDropdownCurrencyItemBinding.inflate(
        inflater,
        parent,
        false
      )
    } else {
      HolderDropdownCurrencyItemBinding.bind(convertView)
    }

    binding.currency = getItem(position)
    binding.executePendingBindings()

    return binding.root
  }
}