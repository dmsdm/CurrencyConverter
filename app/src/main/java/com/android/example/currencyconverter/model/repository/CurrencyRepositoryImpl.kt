package com.android.example.currencyconverter.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.example.currencyconverter.model.entity.Currency

class CurrencyRepositoryImpl(val currencyService: CurrencyService): CurrencyRepository {

    private val currencies = MutableLiveData<List<Currency>>()

    override fun getCurrencies(): LiveData<List<Currency>> {
        return currencies
    }

    override fun getService(): CurrencyService {
        return currencyService
    }

    override fun setData(values: List<Currency>) {
        currencies.postValue(values)
    }
}