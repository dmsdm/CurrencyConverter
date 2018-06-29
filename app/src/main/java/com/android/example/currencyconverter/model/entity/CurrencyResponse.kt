package com.android.example.currencyconverter.model.entity

import java.math.BigDecimal
import java.util.*

data class CurrencyResponse(
        val base: String = "",
        val date: String = "",
        val rates: TreeMap<String, BigDecimal> = TreeMap())