package com.android.example.currencyconverter.model.repository

import com.android.example.currencyconverter.model.entity.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest")
    fun getCurrencies(@Query("base") base: String): Call<CurrencyResponse>
}