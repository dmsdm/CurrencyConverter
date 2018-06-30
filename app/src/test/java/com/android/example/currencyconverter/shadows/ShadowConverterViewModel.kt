package com.android.example.currencyconverter.shadows

import android.app.Application
import com.android.example.currencyconverter.viewmodel.ConverterViewModel
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(ConverterViewModel::class)
class ShadowConverterViewModel {

    @Implementation
    fun __constructor__(application: Application) {
        System.out.println("ConverterViewModel($application)")
    }
}