package com.android.example.currencyconverter.model.repository

interface CurrencyNetworkLoader {
    fun setBase(base: String)
    fun start()
    fun stop()
}