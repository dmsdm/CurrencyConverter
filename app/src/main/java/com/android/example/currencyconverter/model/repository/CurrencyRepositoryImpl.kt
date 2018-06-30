package com.android.example.currencyconverter.model.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.entity.CurrencyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyRepositoryImpl(val service: Service): CurrencyRepository, Callback<CurrencyResponse> {

    private val currencies = MutableLiveData<List<Currency>>()

    override fun getCurrencies(base: String): LiveData<List<Currency>> {
        service.getCurrencies(base).enqueue(this)
        return currencies
    }

    override fun onFailure(call: Call<CurrencyResponse>?, t: Throwable?) {

    }

    override fun onResponse(call: Call<CurrencyResponse>?, response: Response<CurrencyResponse>) {
        val list = ArrayList<Currency>()
        val currencyResponse = response.body()
        currencyResponse?.rates?.entries?.forEach {
            list.add(Currency(it.key, it.value))
        }
        currencies.value = list
    }
}