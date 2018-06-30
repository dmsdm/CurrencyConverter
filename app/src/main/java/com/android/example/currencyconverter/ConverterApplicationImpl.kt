package com.android.example.currencyconverter

import android.app.Application
import com.android.example.currencyconverter.model.repository.CurrencyRepository
import com.android.example.currencyconverter.model.repository.CurrencyRepositoryImpl
import com.android.example.currencyconverter.model.repository.CurrencyService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConverterApplicationImpl: Application(), ConverterApplication {

    private var currencyRepository: CurrencyRepository? = null

    override fun getCurrencyRepository(): CurrencyRepository {
        if (currencyRepository == null) {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://revolut.duckdns.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            currencyRepository = CurrencyRepositoryImpl(retrofit.create(CurrencyService::class.java))
        }
        return currencyRepository as CurrencyRepository
    }
}