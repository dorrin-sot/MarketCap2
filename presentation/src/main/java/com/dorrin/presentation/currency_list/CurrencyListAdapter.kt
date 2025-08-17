package com.dorrin.presentation.currency_list

import android.R
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dorrin.domain.entity.CurrencyEntity

internal class CurrencyListAdapter(
  private val allCurrencies: Array<CurrencyEntity>,
  private var filteredCurrencies: List<CurrencyEntity>? = null,
) : RecyclerView.Adapter<CurrencyListAdapter.ViewHolder>() {

  private val currencies: List<CurrencyEntity>
    get() = filteredCurrencies ?: allCurrencies.toList()

  override fun getItemCount(): Int = currencies.size

  fun getItem(position: Int): CurrencyEntity = currencies[position]

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val context = parent.context
    val view = LayoutInflater.from(context)
      .inflate(R.layout.simple_list_item_2, parent, false)
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

  @SuppressLint("NotifyDataSetChanged")
  fun filter(filteredCurrencies: List<CurrencyEntity>?) {
    this.filteredCurrencies = filteredCurrencies
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView1 = itemView.findViewById<TextView>(R.id.text1)!!
    val textView2 = itemView.findViewById<TextView>(R.id.text2)!!

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
            fromShortName = currency.shortName
            fromLongName = currency.longName
          }
        v?.findNavController()?.navigate(action)
      }
    }
  }
}