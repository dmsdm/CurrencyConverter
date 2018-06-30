package com.android.example.currencyconverter.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.android.example.currencyconverter.LiveDataTestUtil
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.model.repository.CurrencyNetworkLoader
import com.android.example.currencyconverter.model.repository.CurrencyRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
class ConverterInteractorTest {

    private lateinit var interactor: ConverterInteractor
    private lateinit var listener: ConverterInteractor.InteractorListener
    private lateinit var repository: CurrencyRepository
    private lateinit var data: MutableLiveData<List<Currency>>

    @Before
    fun setUp() {
        data = MutableLiveData()
        listener = Mockito.mock(ConverterInteractor.InteractorListener::class.java)
        repository = Mockito.mock(CurrencyRepository::class.java)
        Mockito.`when`(repository.getCurrencies()).thenReturn(data)
        interactor = ConverterInteractor(listener, repository)
    }

    @Test
    fun getCurrencies() {
        val list = getCurrencyList()
        data.value = list

        val expected = ArrayList<Currency>()
        expected.add(Currency("EUR", BigDecimal.ONE))
        expected.addAll(list)
        val actual = LiveDataTestUtil.getValue(interactor.currencies)
        if (actual is ArrayList) {
            assertArrayEquals(expected.toArray(), actual.toArray())
        } else {
            fail()
        }
    }

    private fun getCurrencyList(): List<Currency> {
        val list = ArrayList<Currency>()
        list.add(Currency("test", BigDecimal.ONE))
        return list
    }

    @Test
    fun onError() {
        interactor.onError("error")

        Mockito.verify(listener).onError("error")
    }

    @Test
    fun startUpdates() {
        val loader = Mockito.mock(CurrencyNetworkLoader::class.java)

        interactor.startUpdates(loader)

        Mockito.verify(loader).setBase("EUR")
        Mockito.verify(loader).start()
    }

    @Test
    fun stopUpdates() {
        val loader = Mockito.mock(CurrencyNetworkLoader::class.java)

        interactor.startUpdates(loader)
        interactor.stopUpdates()

        Mockito.verify(loader).stop()
    }

    @Test
    fun setBase() {
        val loader = Mockito.mock(CurrencyNetworkLoader::class.java)

        interactor.startUpdates(loader)
        interactor.setBase("test", BigDecimal.ONE)

        Mockito.verify(loader).setBase("test")
    }

    @Test
    fun setRate() {
        interactor.setRate(BigDecimal.TEN)
        val list = getCurrencyList()
        data.value = list

        val expected = ArrayList<Currency>()
        expected.add(Currency("EUR", BigDecimal.TEN))
        list.forEach{
            expected.add(Currency(it.title, it.value*BigDecimal.TEN))
        }
        val actual = LiveDataTestUtil.getValue(interactor.currencies)
        if (actual is ArrayList) {
            assertArrayEquals(expected.toArray(), actual.toArray())
        } else {
            fail()
        }
    }
}