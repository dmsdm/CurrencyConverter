package com.android.example.currencyconverter.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.android.example.currencyconverter.R
import com.android.example.currencyconverter.model.Currency

class ConverterAdapter(val listener: OnClickListener) : RecyclerView.Adapter<ConverterAdapter.CurrencyHolder>() {

    interface OnClickListener {
        fun onItemClicked(currency: Currency)
    }

    private var currencies: List<Currency> = ArrayList()

    fun setCurrencies(newCurrencies: List<Currency>) {
        if (currencies.isEmpty()) {
            currencies = newCurrencies
            notifyItemRangeInserted(0, currencies.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return currencies.size
                }

                override fun getNewListSize(): Int {
                    return newCurrencies.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return currencies[oldItemPosition].title.equals(newCurrencies[newItemPosition].title)
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return currencies[oldItemPosition].value.equals(newCurrencies[newItemPosition].value)
                }
            })
            currencies = newCurrencies
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        holder.bindView()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false)
        return CurrencyHolder(view)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    inner class CurrencyHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val value: EditText = view.findViewById(R.id.value)
        fun bindView() {
            val currency = currencies[adapterPosition]
            title.text = currency.title
            value.setText("%.2f".format(currency.value))
            itemView.setOnClickListener {
                listener.onItemClicked(currency)
            }
        }
    }
}