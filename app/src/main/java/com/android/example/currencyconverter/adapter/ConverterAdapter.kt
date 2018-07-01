package com.android.example.currencyconverter.adapter

import android.content.Context
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.android.example.currencyconverter.R
import com.android.example.currencyconverter.model.entity.Currency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*


class ConverterAdapter(val listener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClicked(currency: Currency)
        fun onRateChanged(rate: BigDecimal)
    }

    companion object {
        @JvmStatic
        lateinit var baseTitle: String
    }

    object DIFF_CALLBACK: DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency?, newItem: Currency?): Boolean {
            return oldItem?.title.equals(newItem?.title)
        }

        override fun areContentsTheSame(oldItem: Currency?, newItem: Currency?): Boolean {
            return oldItem?.title.equals(baseTitle) || oldItem?.value == newItem?.value
        }
    }

    val differ = AsyncListDiffer<Currency>(this, DIFF_CALLBACK)

    fun submitList(list: List<Currency>) {
        baseTitle = list[0].title
        differ.submitList(list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            else -> 1
        }
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CurrencyHolder) {
            holder.bindView()
        } else if (holder is FirstCurrencyHolder) {
            holder.bindView()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            0 -> {
                var view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_currency_first, parent, false)
                return FirstCurrencyHolder(view)
            }
            else -> {
                var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false)
                return CurrencyHolder(view)
            }

        }
    }

    inner class FirstCurrencyHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val description: TextView = view.findViewById(R.id.description)
        private val value: TextView = view.findViewById(R.id.value)

        fun bindView() {
            val currency = differ.currentList[adapterPosition]
            title.text = currency.title
            description.text = java.util.Currency.getInstance(currency.title).displayName
            value.text = "%.2f".format(Locale.US, currency.value)
            value.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    showKeyboard(v)
                    setTextChangedListener(v)
                } else {
                    removeTextChangedListener(v)
                }
            }
            value.requestFocus()
            itemView.setOnClickListener {
                listener.onItemClicked(currency)
            }
        }

        private fun showKeyboard(v: View) {
            val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            v.post {
                imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    val decimalFormat = DecimalFormat.getInstance()
                    val rate = BigDecimal(decimalFormat.parse(s.toString()).toDouble())
                    listener.onRateChanged(rate)
                } catch (ignore: Exception) {}
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        private fun setTextChangedListener(v: View?) {
            if (v is EditText) {
                v.removeTextChangedListener(textWatcher)
                v.addTextChangedListener(textWatcher)
            }
        }

        private fun removeTextChangedListener(v: View?) {
            if (v is EditText) {
                v.removeTextChangedListener(textWatcher)
            }
        }
    }

    inner class CurrencyHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val description: TextView = view.findViewById(R.id.description)
        private val value: TextView = view.findViewById(R.id.value)

        fun bindView() {
            val currency = differ.currentList[adapterPosition]
            title.text = currency.title
            description.text = java.util.Currency.getInstance(currency.title).displayName
            value.text = "%.2f".format(Locale.US, currency.value)
            itemView.setOnClickListener {
                listener.onItemClicked(currency)
            }
        }
    }
}