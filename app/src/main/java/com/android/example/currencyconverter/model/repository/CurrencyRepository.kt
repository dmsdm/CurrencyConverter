package com.android.example.currencyconverter.model.repository

import android.arch.lifecycle.LiveData
import com.android.example.currencyconverter.model.entity.Currency

interface CurrencyRepository {
    fun getCurrencies(): LiveData<List<Currency>>
    fun getService(): CurrencyService
    fun setData(values: List<Currency>)
}