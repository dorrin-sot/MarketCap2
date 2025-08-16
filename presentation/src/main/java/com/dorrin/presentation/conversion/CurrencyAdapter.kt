package com.dorrin.presentation.conversion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.dorrin.domain.entity.CurrencyEntity

internal class CurrencyAdapter : ArrayAdapter<CurrencyEntity> {
  private val allCurrencies: Array<CurrencyEntity>
  private val currencies: MutableList<CurrencyEntity>

  constructor(context: Context, currencies: List<CurrencyEntity> = listOf()) :
      super(context, android.R.layout.simple_dropdown_item_1line) {
    this.allCurrencies = arrayOf(*currencies.toTypedArray())
    this.currencies = currencies.toMutableList()
  }

  private val _filter = CurrenciesFilter()

  override fun getItem(position: Int): CurrencyEntity = currencies[position]

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
    createBindingView(position, convertView, parent)

  private fun createBindingView(position: Int, convertView: View?, parent: ViewGroup): View {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(
      android.R.layout.simple_dropdown_item_1line,
      parent,
      false
    )
    val item = getItem(position)
    view.findViewById<TextView>(android.R.id.text1).text = item.toString()

    return view
  }

  fun updateData(newList: List<CurrencyEntity>) {
    clear()
    addAll(newList)
    currencies.clear()
    currencies.addAll(newList)
    notifyDataSetChanged()
  }

  override fun getFilter(): Filter = _filter

  private inner class CurrenciesFilter() : Filter() {
    override fun convertResultToString(resultValue: Any?): String =
      "${resultValue as CurrencyEntity}"

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
        filterList.first() !is CurrencyEntity
      ) {
        updateData(allCurrencies.toList())
      } else {
        @Suppress("UNCHECKED_CAST")
        updateData(filterList as List<CurrencyEntity>)
      }
    }
  }
}