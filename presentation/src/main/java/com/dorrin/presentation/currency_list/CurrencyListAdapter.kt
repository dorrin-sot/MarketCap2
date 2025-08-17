package com.dorrin.presentation.currency_list

import android.R
import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Html
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

  private var query: String = ""

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
    holder.textView1.text = Html.fromHtml(formatNameWithQuerySubstring(item.shortName, this.query))
    holder.textView2.text = Html.fromHtml(formatNameWithQuerySubstring(item.longName, this.query))
    holder.tag = item
  }

  private fun formatNameWithQuerySubstring(string: String, substring: String): String {
    if (substring.isEmpty()) return string

    fun fontWithColor(color: Int, input: String): String =
      "<font color=\"${color}\">${input}</font>"

    fun normalFont(input: String): String = fontWithColor(Color.BLACK, input)
    fun highlightedFont(input: String): String = fontWithColor(Color.RED, input)

    val ranges = substring.toRegex(setOf(RegexOption.IGNORE_CASE))
      .findAll(string)
      .map { it.range.start to it.range.endExclusive }
      .toList()

    if (ranges.isEmpty()) return string

    return buildString {
      append(normalFont(string.substring(0, ranges.first().first)))
      append(ranges.joinToString("") { (start, end) ->
        normalFont(string.substring(0, start))
        highlightedFont(string.substring(start, end))
      })
      append(normalFont(string.substring(ranges.last().second)))
    }
  }

  @SuppressLint("NotifyDataSetChanged")
  fun filter(query: String?, filteredCurrencies: List<CurrencyEntity>?) {
    this.query = query ?: ""
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