package com.android.example.currencyconverter.model.repository

import android.os.AsyncTask
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.model.entity.Currency


class CurrencyNetworkLoaderImpl(private val listener: CurrencyLoaderListener,
                                private val repository: CurrencyRepository? = null) :
        AsyncTask<Void, List<Currency>, List<Currency>>(), CurrencyNetworkLoader {

    interface CurrencyLoaderListener {
        @WorkerThread
        fun onError(error: String)
    }

    private val lock = java.lang.Object()
    @Volatile private lateinit var base: String

    override fun setBase(base: String) {
        synchronized(lock) {
            this.base = base
            lock.notify()
        }
    }

    override fun start() {
        execute()
    }

    override fun stop() {
        cancel(false)
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
        val base = this.base
        val response = repository?.getService()?.getCurrencies(base)?.execute()
        if (response?.isSuccessful!!) {
            val currencyResponse = response.body()
            currencyResponse?.rates?.entries?.forEach {
                list.add(Currency(it.key, it.value))
            }
            if (base == this.base) { // skip if base changed
                repository?.setData(list)
            }
        } else {
            val error = response.errorBody()
            listener.onError(error.toString())
        }
    }
}