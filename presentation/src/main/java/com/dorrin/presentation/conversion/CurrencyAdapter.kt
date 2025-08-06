package com.dorrin.presentation.conversion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.dorrin.domain.model.Currency
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.HolderDropdownCurrencyItemBinding

internal class CurrencyAdapter(
  context: Context,
  private val currencies: List<Currency>
) : ArrayAdapter<Currency>(
  context,
  R.layout.holder_dropdown_currency_item,
  currencies,
) {
  private val _filter = CurrenciesFilter()

  override fun getItemId(position: Int): Long = getItem(position)?.id ?: 0L

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  private fun createBindingView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding = if (convertView == null) {
      val inflater = LayoutInflater.from(parent.context)

      HolderDropdownCurrencyItemBinding.inflate(
        inflater,
        parent,
        false
      )
    } else {
      HolderDropdownCurrencyItemBinding.bind(convertView)
    }

    val item = getItem(position)!!
    binding.currency = item
    binding.executePendingBindings()

//    binding.root.tag = item

    return binding.root
  }

  override fun getFilter(): Filter = _filter

  private inner class CurrenciesFilter() : Filter() {
    override fun convertResultToString(resultValue: Any?): String =
      (resultValue as Currency).let { "${it.country.flag} ${it.shortName} ${it.longName}" }

    override fun performFiltering(constraint: CharSequence?): FilterResults? {
      val filterResults = FilterResults()
      if (constraint == null) return filterResults

      val filtered = currencies
        .filter {
          convertResultToString(it)
            .lowercase()
            .contains("$constraint".lowercase())
        }
      filterResults.values = filtered
      filterResults.count = filtered.size
      return filterResults
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
      try {
        results ?: return
        clear()

        val filterList = results.values as List<Currency>
        filterList.ifEmpty { null } ?: return

        addAll(filterList)
      } finally {
        notifyDataSetChanged()
      }
    }
  }
}