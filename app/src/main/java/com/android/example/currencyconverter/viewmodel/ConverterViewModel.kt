package com.android.example.currencyconverter.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.VisibleForTesting
import android.support.annotation.WorkerThread
import com.android.example.currencyconverter.ConverterApplication
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.repository.CurrencyNetworkLoaderImpl
import java.math.BigDecimal

class ConverterViewModel(application: Application, test: Any? = null) : AndroidViewModel(application),
        ConverterInteractor.InteractorListener {

    val currencies : MediatorLiveData<List<Currency>> = MediatorLiveData()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
    val scrollToPosition: SingleLiveEvent<Int> = SingleLiveEvent()
    @VisibleForTesting lateinit var interactor: ConverterInteractor
    private var shouldScroll = false


    constructor(application: Application) : this(application, null) {
        if (application is ConverterApplication) {
            val currencyRepository = application.getCurrencyRepository()
            interactor = ConverterInteractor(this, currencyRepository)
            interactor.startUpdates(CurrencyNetworkLoaderImpl(interactor, currencyRepository))
            currencies.addSource(interactor.currencies) { list ->
                if (list != null) {
                    setList(list)
                }
            }
        }
    }

    @VisibleForTesting fun setList(list: List<Currency>) {
        currencies.value = list
        if (shouldScroll) {
            shouldScroll = false
            scrollToPosition.value = 0
        }
    }

    override fun onCleared() {
        interactor.stopUpdates()
    }

    @WorkerThread
    override fun onError(message: String) {
        error.postValue(message)
    }

    fun onItemClicked(currency: Currency) {
        shouldScroll = true
        interactor.setBase(currency.title, currency.value)
    }

    fun onRateChanged(rate: BigDecimal) {
        interactor.setRate(rate)
    }
}