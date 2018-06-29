package com.android.example.currencyconverter.viewmodel

import android.os.AsyncTask
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.repository.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal


class CurrencyLoader(private val listener: CurrencyLoaderListener) : AsyncTask<Void, List<Currency>, List<Currency>>() {

    interface CurrencyLoaderListener {
        @MainThread
        fun onProgressChange(list: List<Currency>)
        @WorkerThread
        fun onError(error: String)

    }

    private lateinit var service: Service
    init {
        //Init retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(Service::class.java)
    }

    private val lock = java.lang.Object()
    @Volatile private var base: String = "EUR"
    @Volatile private var rate: BigDecimal = BigDecimal.ONE

    fun setRate(rate: BigDecimal) {
        synchronized(lock) {
            this.rate = rate
            lock.notify()
        }
    }

    fun setRate(base: String, rate: BigDecimal) {
        synchronized(lock) {
            this.base = base
            this.rate = rate
            lock.notify()
        }
    }

    override fun doInBackground(vararg params: Void?): List<Currency> {
        while (!isCancelled) {
            loadFromNetwork()
            synchronized(lock) {
                lock.wait(1000)
            }
        }
        return ArrayList()
    }

    private fun loadFromNetwork() {
        val list = ArrayList<Currency>()
        var base = ""
        var rate = BigDecimal.ZERO
        synchronized(lock) {
            base = this.base
            rate = this.rate
        }
        list.add(Currency(base, rate))
        val response = service.getCurrencies(base).execute()
        if (response.isSuccessful) {
            val currencyResponse = response.body()
            currencyResponse?.rates?.entries?.forEach {
                list.add(Currency(it.key, it.value * rate))
            }
            publishProgress(list)
        } else {
            val error = response.errorBody()
            listener.onError(error.toString())
        }
    }

    override fun onProgressUpdate(vararg values: List<Currency>) {
        listener.onProgressChange(values[0])
    }
}