package me.tyschenko.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import me.tyschenko.data.model.CurrencyRate

class CurrencyAdapter(private val currencyValueListener: CurrencyValueListener) : RecyclerView.Adapter<CurrencyViewHolder>() {

    private var currencyRates: List<CurrencyRate> = emptyList()
    private var currencyOrderList = (0 until itemCount).toMutableList() // Contains order of currencies in adapter

    fun setCurrencyRates(currencyRates: List<CurrencyRate>) {
        this.currencyRates = currencyRates
        if (currencyOrderList.size != itemCount) {
            currencyOrderList = (0 until itemCount).toMutableList()
        }
        notifyItemRangeChanged(0, itemCount, CURRENCY_VALUE_CHANGED_PAYLOAD)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency, parent, false))
    }

    override fun getItemCount(): Int {
        return currencyRates.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.contains(CURRENCY_VALUE_CHANGED_PAYLOAD)) {
            if (!holder.amountEditText.isFocused) {
                holder.amountEditText.setText(getCurrencyRateForAdapterPosition(position).getCurrencyAmountAsString())
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyRate = getCurrencyRateForAdapterPosition(position)
        with(holder) {
            baseTextView.text = currencyRate.base
            amountEditText.setText(currencyRate.getCurrencyAmountAsString())

            itemView.setOnClickListener {
                moveItemToTop(getPositionOfCurrencyRate(currencyRate.base))
                focusValueEditText()
            }

            amountEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    moveItemToTop(getPositionOfCurrencyRate(currencyRate.base))
                }
            }

            amountEditText.doOnTextChanged { valueText, _, _, _ ->
                if (!valueText.isNullOrEmpty() && amountEditText.isFocused && valueText.toString() != ".") {
                    val value = valueText.toString().toDouble()
                    currencyValueListener.onCurrencyValueChanged(currencyRate.base, value)
                }
            }
        }
    }

    private fun moveItemToTop(itemPosition: Int) {
        currencyOrderList.add(0, currencyOrderList[itemPosition])
        currencyOrderList.removeAt(itemPosition + 1)
        notifyItemMoved(itemPosition, 0)
        currencyValueListener.onCurrencyValueClicked()
    }

    private fun getCurrencyRateForAdapterPosition(position: Int): CurrencyRate = currencyRates[currencyOrderList[position]]

    private fun getPositionOfCurrencyRate(currencyBase: String): Int = currencyOrderList.indexOf(
            currencyRates.indexOfFirst { it.base == currencyBase }
    )

    private companion object {
        const val CURRENCY_VALUE_CHANGED_PAYLOAD = 1 shl 8
    }
}