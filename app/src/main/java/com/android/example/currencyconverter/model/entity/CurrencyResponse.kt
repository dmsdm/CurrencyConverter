package com.android.example.currencyconverter.model.entity

import java.util.*

data class CurrencyResponse(
        val base: String = "",
        val date: String = "",
        val rates: TreeMap<String, Float> = TreeMap())