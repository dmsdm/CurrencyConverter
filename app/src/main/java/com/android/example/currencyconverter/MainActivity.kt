package com.android.example.currencyconverter

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.android.example.currencyconverter.adapter.ConverterAdapter
import com.android.example.currencyconverter.model.entity.Currency
import com.android.example.currencyconverter.viewmodel.ConverterViewModel
import java.math.BigDecimal

class MainActivity : AppCompatActivity(), ConverterAdapter.OnClickListener {

    @VisibleForTesting lateinit var recyclerView: RecyclerView
    @VisibleForTesting lateinit var viewAdapter: ConverterAdapter
    @VisibleForTesting lateinit var viewManager: RecyclerView.LayoutManager
    @VisibleForTesting lateinit var viewModel: ConverterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ConverterAdapter(this)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel = ViewModelProviders.of(this).get(ConverterViewModel::class.java)

        subscribeUi()
    }
    private fun subscribeUi() {
        viewModel.currencies.observe(this, Observer<List<Currency>> { list ->
            if (list != null) {
                viewAdapter.submitList(list)
            }
        })
        viewModel.error.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })
        viewModel.scrollToPosition.observe(this, Observer { position ->
            recyclerView.postDelayed({
                position?.let { viewManager.smoothScrollToPosition(recyclerView, null, it) }
            }, 500)
        })
    }

    override fun onItemClicked(currency: Currency) {
        viewModel.onItemClicked(currency)
    }

    override fun onRateChanged(rate: BigDecimal) {
        viewModel.onRateChanged(rate)
    }
}