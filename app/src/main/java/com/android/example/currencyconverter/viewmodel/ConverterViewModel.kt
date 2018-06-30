package com.android.example.currencyconverter.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.VisibleForTesting
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.repository.CurrencyLoader
import java.math.BigDecimal

class ConverterViewModel(application: Application, test: Any? = null) : AndroidViewModel(application),
        CurrencyLoader.CurrencyLoaderListener {

    val currencies : MutableLiveData<List<Currency>> = MutableLiveData()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
    val scrollToPosition: SingleLiveEvent<Int> = SingleLiveEvent()
    @VisibleForTesting lateinit var loader: CurrencyLoader
    private var shouldScroll = false


    constructor(application: Application) : this(application, null) {
        loader = CurrencyLoader(this)
        loader.execute()
    }

    override fun onCleared() {
        loader.cancel(false)
    }

    @MainThread
    override fun onProgressChange(list: List<Currency>) {
        currencies.value = list
        if (shouldScroll) {
            shouldScroll = false
            scrollToPosition.value = 0
        }
    }

    @WorkerThread
    override fun onError(message: String) {
        error.postValue(message)
    }

    fun onItemClicked(currency: Currency) {
        shouldScroll = true
        loader.setRate(currency.title, BigDecimal.ONE)
    }

    fun onRateChanged(rate: BigDecimal) {
        loader.setRate(rate)
    }
}