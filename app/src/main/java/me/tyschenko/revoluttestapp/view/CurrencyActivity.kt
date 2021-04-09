package me.tyschenko.revoluttestapp.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.tyschenko.revoluttestapp.R
import me.tyschenko.data.model.CurrencyRate
import me.tyschenko.feature.adapter.CurrencyAdapter
import me.tyschenko.feature.adapter.CurrencyValueListener
import me.tyschenko.revoluttestapp.presenter.CurrencyActivityPresenter
import javax.inject.Inject

class CurrencyActivity : DaggerAppCompatActivity(), CurrencyActivityView {

    @Inject
    lateinit var currencyActivityPresenter: CurrencyActivityPresenter

    private var currencyAdapter = CurrencyAdapter(object : CurrencyValueListener {
        override fun onCurrencyValueChanged(base: String, value: Double) {
            currencyActivityPresenter.onCurrencyValueChanged(base, value)
        }

        override fun onCurrencyValueClicked() {
            currencyRecyclerView?.scrollToPosition(0)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currencyRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currencyAdapter
        }
        currencyActivityPresenter.onCreate()
    }

    override fun onDestroy() {
        currencyActivityPresenter.onDestroy()
        super.onDestroy()
    }

    override fun updateCurrencyRatesOnScreen(currencyRates: List<CurrencyRate>) {
        hideProgressBar()
        currencyAdapter.setCurrencyRates(currencyRates)
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        currencyRecyclerView.visibility = View.VISIBLE
    }
}
