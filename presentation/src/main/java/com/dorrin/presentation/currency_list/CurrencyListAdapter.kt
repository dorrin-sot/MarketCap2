package com.dorrin.presentation.currency_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dorrin.domain.entity.CurrencyEntity

internal class CurrencyListAdapter(
  private val allCurrencies: Array<CurrencyEntity>,
) : RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {
  override fun getItemCount(): Int = allCurrencies.size

  fun getItem(position: Int): CurrencyEntity = allCurrencies[position]

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(android.R.layout.simple_list_item_2, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.textView1.text = getItem(position).shortName
    holder.textView2.text = getItem(position).longName
  }

  override fun getItemId(position: Int): Long = getItem(position).id

  internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView1 = itemView.findViewById<TextView>(android.R.id.text1)!!
    val textView2 = itemView.findViewById<TextView>(android.R.id.text2)!!
  }
}