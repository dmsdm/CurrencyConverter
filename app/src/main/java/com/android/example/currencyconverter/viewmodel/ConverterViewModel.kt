package com.android.example.currencyconverter.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.model.Currency

class ConverterViewModel(application: Application) : AndroidViewModel(application), CurrencyLoader.CurrencyLoaderListener {

    val currencies : MutableLiveData<List<Currency>> = MutableLiveData()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
    val scrollToPosition: SingleLiveEvent<Int> = SingleLiveEvent()
    private val loader: CurrencyLoader = CurrencyLoader(this)
    private var shouldScroll = false

    init {
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
        loader.setRate(currency.title, 1f)
        shouldScroll = true
    }
}