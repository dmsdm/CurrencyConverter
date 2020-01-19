package com.android.example.currencyconverter.model.repository

import com.android.example.currencyconverter.model.entity.CurrencyResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("latest")
    fun getCurrencies(@Query("base") base: String): Observable<CurrencyResponse>
}