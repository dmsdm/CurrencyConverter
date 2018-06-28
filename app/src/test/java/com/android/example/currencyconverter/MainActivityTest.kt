package com.android.example.currencyconverter

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
}