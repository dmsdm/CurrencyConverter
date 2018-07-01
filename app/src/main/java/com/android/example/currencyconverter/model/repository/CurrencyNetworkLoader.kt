package com.android.example.currencyconverter.model.repository

import android.support.annotation.WorkerThread

interface CurrencyNetworkLoader {
    fun setBase(base: String)
    fun start()
    fun stop()
    interface CurrencyLoaderListener {
        @WorkerThread
        fun onError(error: String)
    }
}