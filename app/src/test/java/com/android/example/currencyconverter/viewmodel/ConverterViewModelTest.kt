package com.android.example.currencyconverter.viewmodel

import com.android.example.currencyconverter.LiveDataTestUtil
import com.android.example.currencyconverter.model.entity.Currency
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
class ConverterViewModelTest {

    private lateinit var viewModel: ConverterViewModel

    @Before
    fun setUp() {
        viewModel = ConverterViewModel(RuntimeEnvironment.application, null)
        viewModel.interactor = Mockito.mock(ConverterInteractor::class.java)
    }

    @Test
    fun getCurrencies() {
        assertNotNull(viewModel.currencies)
    }

    @Test
    fun getError() {
        assertNotNull(viewModel.error)
    }

    @Test
    fun onError() {
        viewModel.onError("error")

        val actual = LiveDataTestUtil.getValue(viewModel.error)
        assertEquals("error", actual)
    }

    @Test
    fun scrollToZero_whenOnItemClicked() {
        val currency = Currency("test", BigDecimal.ONE)
        val list = ArrayList<Currency>()
        list.add(currency)
        `when`(viewModel.interactor.setBase(currency.title, currency.value)).then {
            viewModel.setList(list)
        }

        viewModel.onItemClicked(currency)

        val position = LiveDataTestUtil.getValue(viewModel.scrollToPosition)
        assertEquals(0, position)
        assertArraysEqual(list)
    }

    private fun assertArraysEqual(list: ArrayList<Currency>) {
        val actual = LiveDataTestUtil.getValue(viewModel.currencies)
        if (actual is ArrayList) {
            assertArrayEquals(list.toArray(), actual.toArray())
        } else {
            fail()
        }
    }

    @Test
    fun onRateChanged() {
        viewModel.onRateChanged(BigDecimal.TEN)

        Mockito.verify(viewModel.interactor).setRate(BigDecimal.TEN)
    }
}