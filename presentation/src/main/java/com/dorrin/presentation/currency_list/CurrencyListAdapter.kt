package com.dorrin.presentation.currency_list

import android.R
import android.annotation.SuppressLint
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
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
    holder.text1 = item.shortName
    holder.text2 = item.longName
    holder.tag = item
  }

  @SuppressLint("NotifyDataSetChanged")
  fun filter(query: String?, filteredCurrencies: List<CurrencyEntity>?) {
    this.query = query ?: ""
    this.filteredCurrencies = filteredCurrencies
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView1 = itemView.findViewById<TextView>(R.id.text1)!!
    private val textView2 = itemView.findViewById<TextView>(R.id.text2)!!

    var text1: String
      get() = textView1.text.toString()
      set(value) {
        setText(textView1, value)
      }

    var text2: String
      get() = textView2.text.toString()
      set(value) {
        setText(textView2, value)
      }

    private fun setText(textView: TextView, value: String) {
      val theme = itemView.context.theme
      val defaultColor = TypedValue().let {
        theme.resolveAttribute(R.attr.textColorPrimary, it, true)
        it.data
      }
      val highlightColor = TypedValue().let {
        theme.resolveAttribute(
          com.google.android.material.R.attr.colorSecondary,
          it,
          true
        )
        it.data
      }

      textView.text = Html.fromHtml(
        TextHighlighter(
          value,
          this@CurrencyListAdapter.query,
          defaultColor,
          highlightColor
        ).buildHighlightedText(),
        FROM_HTML_MODE_LEGACY
      )
    }

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

  private class TextHighlighter(
    val string: String,
    val substring: String,
    @ColorInt val defaultColor: Int,
    @ColorInt val highlightColor: Int,
  ) {
    fun buildHighlightedText(): String {
      if (substring.isEmpty()) return string

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

    private fun fontWithColor(color: Int, input: String): String =
      "<font color=\"${color}\">${input}</font>"

    private fun normalFont(input: String): String = fontWithColor(defaultColor, input)

    private fun highlightedFont(input: String): String = fontWithColor(highlightColor, input)
  }
}