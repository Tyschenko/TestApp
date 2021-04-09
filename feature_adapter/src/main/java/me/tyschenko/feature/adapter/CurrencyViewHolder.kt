package me.tyschenko.feature.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val baseTextView: AppCompatTextView = view.baseTextView
    val amountEditText: AppCompatEditText = view.amountEditText

    fun focusValueEditText() {
        amountEditText.requestFocus()
        amountEditText.setSelection(amountEditText.text?.length ?: 0)
    }
}