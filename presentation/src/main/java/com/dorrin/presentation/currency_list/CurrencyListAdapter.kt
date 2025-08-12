package com.dorrin.presentation.currency_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dorrin.domain.entity.CurrencyEntity

internal class CurrencyListAdapter(
  private val allCurrencies: Array<CurrencyEntity>,
) : RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {
  override fun getItemCount(): Int = allCurrencies.size

  fun getItem(position: Int): CurrencyEntity = allCurrencies[position]

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val context = parent.context
    val view = LayoutInflater.from(context)
      .inflate(android.R.layout.simple_list_item_2, parent, false)
    val holder = ViewHolder(view)

    view.setOnClickListener(holder.OnClickListener())

    return holder
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    holder.textView1.text = item.shortName
    holder.textView2.text = item.longName
    holder.tag = item
  }

  override fun getItemId(position: Int): Long = getItem(position).id

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView1 = itemView.findViewById<TextView>(android.R.id.text1)!!
    val textView2 = itemView.findViewById<TextView>(android.R.id.text2)!!

    var tag: CurrencyEntity
      get() = itemView.tag as CurrencyEntity
      set(value) {
        itemView.tag = value
      }

    inner class OnClickListener : View.OnClickListener {
      override fun onClick(v: View?) {
        val currency = tag

        val action = CurrencyListFragmentDirections.actionNavigationListToNavigationConversion()
          .apply {
            fromId = currency.id
            fromShortName = currency.shortName
            fromLongName = currency.longName
          }
        v?.findNavController()?.navigate(action)
      }
    }
  }
}