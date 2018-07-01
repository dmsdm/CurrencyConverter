package com.android.example.currencyconverter.model.repository

import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.entity.CurrencyResponse
import com.nhaarman.mockitokotlin2.argumentCaptor
import okhttp3.Request
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.*

@RunWith(RobolectricTestRunner::class)
class CurrencyNetworkLoaderImplTest {

    private lateinit var currencyNetworkLoader: CurrencyNetworkLoaderImpl
    private lateinit var listener: CurrencyNetworkLoader.CurrencyLoaderListener
    private lateinit var repository: CurrencyRepository
    private lateinit var service: CurrencyService

    @Before
    fun setUp() {
        listener = Mockito.mock(CurrencyNetworkLoader.CurrencyLoaderListener::class.java)
        repository = Mockito.mock(CurrencyRepository::class.java)
        currencyNetworkLoader = CurrencyNetworkLoaderImpl(listener, repository)
        currencyNetworkLoader.setBase("EUR")

        service = Mockito.mock(CurrencyService::class.java)
        Mockito.`when`(repository.getService()).thenReturn(service)
        Mockito.`when`(service.getCurrencies("EUR")).thenReturn(getResponseCall())
    }

    private fun getResponseCall(): Call<CurrencyResponse>? {
        return object :Call<CurrencyResponse> {
            private var isCanceled = false
            override fun enqueue(callback: Callback<CurrencyResponse>?) {}
            override fun isExecuted(): Boolean {return true}
            override fun clone(): Call<CurrencyResponse> {return this}
            override fun isCanceled(): Boolean {return isCanceled}
            override fun cancel() {isCanceled = true}
            override fun execute(): Response<CurrencyResponse> {
                return Response.success(getResponse())
            }
            override fun request(): Request {
                return Request.Builder().build()
            }
        }
    }

    private fun getResponse(): CurrencyResponse? {
        val map = TreeMap<String, BigDecimal>()
        map["RUB"] = BigDecimal(75)
        return CurrencyResponse("EUR", "date", map)
    }

    @Test
    fun setBase() {
        Mockito.`when`(service.getCurrencies("RUB")).thenReturn(getResponseCall())
        stopLoaderAfter(100)

        currencyNetworkLoader.setBase("RUB")
        currencyNetworkLoader.start()

        Mockito.verify(service).getCurrencies("RUB")
    }

    @Test
    fun start() {
        stopLoaderAfter(100)
        currencyNetworkLoader.start()

        val captor = argumentCaptor<List<Currency>>()
        Mockito.verify(repository).setData(captor.capture())
        assertEquals(Currency("RUB", BigDecimal(75)), captor.lastValue[0])
    }

    private fun stopLoaderAfter(ms: Long) {
        Thread {
            Thread.sleep(ms)
            currencyNetworkLoader.stop()
            synchronized(currencyNetworkLoader.lock) {
                currencyNetworkLoader.lock.notify()
            }
        }.start()
    }
}