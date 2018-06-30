package com.android.example.currencyconverter.model.repository

import android.arch.lifecycle.LiveData
import com.android.example.currencyconverter.model.entity.Currency

interface CurrencyRepository {
    fun getCurrencies(base: String): LiveData<List<Currency>>
}