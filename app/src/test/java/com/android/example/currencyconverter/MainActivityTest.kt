package com.android.example.currencyconverter

import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.shadows.ShadowConverterViewModel
import com.android.example.currencyconverter.viewmodel.ConverterViewModel
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(shadows = [ShadowConverterViewModel::class])
class MainActivityTest {

    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun hasRecyclerView() {
        assertNotNull(activity.recyclerView)
    }

    @Test
    fun hasAdapter() {
        assertNotNull(activity.viewAdapter)
    }

    @Test
    fun hasLayoutManager() {
        assertNotNull(activity.viewManager)
    }

    @Test
    fun hasViewModel() {
        assertNotNull(activity.viewModel)
    }

    @Test
    fun onItemClicked() {
        activity.viewModel = Mockito.mock(ConverterViewModel::class.java)

        activity.onItemClicked(Currency("RUB", BigDecimal.ONE))

        Mockito.verify(activity.viewModel).onItemClicked(Currency("RUB", BigDecimal.ONE))
    }

    @Test
    fun onRateChanged() {
        activity.viewModel = Mockito.mock(ConverterViewModel::class.java)

        activity.onRateChanged(BigDecimal.TEN)

        Mockito.verify(activity.viewModel).onRateChanged(BigDecimal.TEN)
    }
}