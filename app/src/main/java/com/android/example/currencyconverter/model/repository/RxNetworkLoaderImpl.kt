package com.android.example.currencyconverter.model.repository

import com.android.example.currencyconverter.model.entity.Currency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RxNetworkLoaderImpl(private val listener: CurrencyNetworkLoader.CurrencyLoaderListener,
                          private val repository: CurrencyRepository? = null) :
        CurrencyNetworkLoader {

    private var networkObservable: Disposable? = null
    private lateinit var base: String
    private val forceSubject = PublishSubject.create<Long>()

    override fun setBase(base: String) {
        this.base = base
        forceSubject.onNext(1)
    }

    override fun start() {
        networkObservable = Observable.interval(1, TimeUnit.SECONDS)
                .mergeWith(forceSubject)
                .flatMap { repository?.getService()?.getCurrencies(base) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { response ->
                    response?.rates?.entries?.map {
                        Currency(it.key, it.value)
                    }?.let {
                        currencies -> repository?.setData(currencies)
                    }
                }
                .doOnError { throwable -> listener.onError(throwable.toString()) }
                .subscribe()
    }

    override fun stop() {
        networkObservable?.dispose()
    }
}