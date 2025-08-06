package com.dorrin.presentation.conversion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.dorrin.domain.model.Currency
import com.dorrin.presentation.R
import com.dorrin.presentation.databinding.HolderDropdownCurrencyItemBinding

internal class CurrencyAdapter : ArrayAdapter<Currency> {
  private val allCurrencies: Array<Currency>
  private val currencies: MutableList<Currency>

  constructor(context: Context, currencies: List<Currency> = listOf()) :
      super(context, R.layout.holder_dropdown_currency_item) {
    this.allCurrencies = arrayOf(*currencies.toTypedArray())
    this.currencies = currencies.toMutableList()
  }

  private val _filter = CurrenciesFilter()

  override fun getItem(position: Int): Currency = currencies[position]

  override fun getItemId(position: Int): Long = getItem(position).id

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  private fun createBindingView(position: Int, convertView: View?, parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val binding = HolderDropdownCurrencyItemBinding.inflate(inflater, parent, false)

    val item = getItem(position)
    binding.currency = item
    binding.lifecycleOwner = parent.findViewTreeLifecycleOwner()
    binding.executePendingBindings()

    return binding.root
  }

  fun updateData(newList: List<Currency>) {
    clear()
    addAll(newList)
    notifyDataSetChanged()
  }

  override fun getFilter(): Filter = _filter

  private inner class CurrenciesFilter() : Filter() {
    override fun convertResultToString(resultValue: Any?): String = "${resultValue as Currency}"

    override fun performFiltering(constraint: CharSequence?): FilterResults? =
      FilterResults().apply {
        val filtered = allCurrencies
          .filter {
            convertResultToString(it)
              .contains(constraint ?: "", ignoreCase = true)
          }
        values = filtered
        count = filtered.size
      }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
      val filterList = results?.values
      if (filterList == null ||
        filterList !is List<*> ||
        filterList.isEmpty() ||
        filterList.first() !is Currency
      ) {
        updateData(allCurrencies.toList())
      } else {
        @Suppress("UNCHECKED_CAST")
        updateData(filterList as List<Currency>)
      }
    }
  }
}