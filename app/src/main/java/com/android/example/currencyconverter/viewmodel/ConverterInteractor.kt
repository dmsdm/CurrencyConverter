package com.android.example.currencyconverter.viewmodel

import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.repository.CurrencyNetworkLoader
import com.android.example.currencyconverter.model.repository.CurrencyNetworkLoaderImpl
import com.android.example.currencyconverter.model.repository.CurrencyRepository
import java.math.BigDecimal

class ConverterInteractor(val listener: InteractorListener,
                          val currencyRepository: CurrencyRepository) :
        CurrencyNetworkLoaderImpl.CurrencyLoaderListener {

    interface InteractorListener {
        @WorkerThread
        fun onError(message: String)
    }

    val currencies: MediatorLiveData<List<Currency>> = MediatorLiveData()
    private lateinit var loader: CurrencyNetworkLoader
    private var base: String = "EUR"
    private var rate: BigDecimal = BigDecimal.ONE

    init {
        currencies.addSource(currencyRepository.getCurrencies()) { list ->
            setCurrencies(list)
        }
    }

    @WorkerThread
    override fun onError(error: String) {
        listener.onError(error)
    }

    fun startUpdates(loader: CurrencyNetworkLoader) {
        this.loader = loader
        loader.setBase(base)
        loader.start()
    }

    private fun setCurrencies(list: List<Currency>?) {
        val newList = ArrayList<Currency>()
        newList.add(Currency(base, rate))
        list?.forEach {
            newList.add(Currency(it.title, it.value*rate))
        }
        currencies.value = newList
    }

    fun stopUpdates() {
        loader.stop()
    }

    fun setBase(base: String, rate: BigDecimal) {
        this.base = base
        this.rate = rate
        loader.setBase(base)
    }

    fun setRate(value: BigDecimal) {
        this.rate = value
        val list = currencyRepository.getCurrencies().value
        setCurrencies(list)
    }
}