package com.android.example.currencyconverter

import com.android.example.currencyconverter.model.repository.CurrencyRepository

interface ConverterApplication {
    fun getCurrencyRepository(): CurrencyRepository
}