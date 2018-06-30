package com.android.example.currencyconverter.model.entity

import java.math.BigDecimal

class Currency(val title: String, val value: BigDecimal) {
    override fun equals(other: Any?): Boolean {
        if (other is Currency) {
            return title == other.title && value == other.value
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}