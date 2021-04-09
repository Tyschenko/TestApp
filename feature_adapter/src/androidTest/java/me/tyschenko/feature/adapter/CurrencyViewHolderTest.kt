package me.tyschenko.feature.adapter

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ViewHelpers
import com.karumi.shot.ScreenshotTest
import me.tyschenko.data.model.CurrencyRate
import org.junit.Test

/**
 * Run all screenshot tests on Pixel 2 XL Api 27
 *
 * To run screenshot tests use this command in terminal:
 * ./gradlew executeScreenshotTests -Precord
 */
internal class CurrencyViewHolderTest : ScreenshotTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun noAmount() {
        val currencyRate = CurrencyRate(base = "EUR", amount = 0.0)
        createViewHolder(currencyRate).snapAndRecord()
    }

    @Test
    fun smallAmount() {
        val currencyRate = CurrencyRate(base = "EUR", amount = 100.0)
        createViewHolder(currencyRate).snapAndRecord()
    }

    @Test
    fun amountWithFractionalPart() {
        val currencyRate = CurrencyRate(base = "EUR", amount = 123.45)
        createViewHolder(currencyRate).snapAndRecord()
    }

    @Test
    fun amountForMaxLength() {
        val currencyRate = CurrencyRate(base = "EUR", amount = 123456123456.0) // android:maxLength="12"
        createViewHolder(currencyRate).snapAndRecord()
    }

    @Test
    fun amountLongerThanMaxLength() {
        val currencyRate = CurrencyRate(base = "EUR", amount = Double.MAX_VALUE) // android:maxLength="12"
        createViewHolder(currencyRate).snapAndRecord()
    }

    private fun createViewHolder(currencyRate: CurrencyRate): CurrencyViewHolder {
        val viewHolder = CurrencyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_currency, LinearLayout(context)))
        viewHolder.baseTextView.text = currencyRate.base
        viewHolder.amountEditText.setText(currencyRate.getCurrencyAmountAsString())
        return viewHolder
    }

    private fun RecyclerView.ViewHolder.snapAndRecord() {
        val viewHelpers = ViewHelpers.setupView(itemView)
        viewHelpers.setExactWidthDp(300)
        viewHelpers.layout()
        Screenshot.snap(itemView).record()
    }
}