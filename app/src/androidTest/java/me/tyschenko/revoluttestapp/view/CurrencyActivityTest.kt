package me.tyschenko.revoluttestapp.view

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import me.tyschenko.revoluttestapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class CurrencyActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<CurrencyActivity> = ActivityScenarioRule(CurrencyActivity::class.java)

    private val recyclerView by lazy { onView(withId(R.id.currencyRecyclerView)) }

    private val progressBar by lazy { onView(withId(R.id.progressBar)) }

    @Test
    fun progressVisibleWhenInitialLoading() {
        progressBar.checkVisible()
        recyclerView.checkInvisible()

        waitSomeTime()

        // After data loaded from network, recycler becomes visible and progressbar hidden
        recyclerView.checkVisible()
        progressBar.checkInvisible()
    }

    @Test
    fun verifyItemMovedToTopAfterClick() {
        waitSomeTime()
        scrollToCurrency("EUR")
        clickOnCurrencyItem("EUR")
        assertCurrencyCodeOnPosition(currencyCode = "EUR", position = 0)

        // Scroll to the bottom and click on last currency
        scrollToCurrency("ZAR")
        clickOnCurrencyItem("ZAR")
        scrollToCurrency("ZAR")

        // Verify that ZAR is first item now and EUR moved to second
        assertCurrencyCodeOnPosition(currencyCode = "ZAR", position = 0)
        assertCurrencyCodeOnPosition(currencyCode = "EUR", position = 1)

        // Click on EUR again and verify that ZAR is second now
        scrollToCurrency("EUR")
        clickOnCurrencyItem("EUR")
        assertCurrencyCodeOnPosition(currencyCode = "EUR", position = 0)
        assertCurrencyCodeOnPosition(currencyCode = "ZAR", position = 1)

        // Click on EUR again and verify that it is still on first position
        clickOnCurrencyItem("EUR")
        assertCurrencyCodeOnPosition(currencyCode = "EUR", position = 0)
    }

    @Test
    fun typeZeroAmount() {
        waitSomeTime()
        scrollToCurrency("USD")
        typeOnCurrencyItem("USD", 0)
        waitSomeTime()
        assertAmountAtPosition(amount = 0, position = 0)
        assertAmountAtPosition(amount = 0, position = 1)
        assertAmountAtPosition(amount = 0, position = 30)
    }

    @Test
    fun typeNonZeroAmount() {
        val amount = 123
        waitSomeTime()
        scrollToCurrency("USD")
        typeOnCurrencyItem("USD", amount)
        waitSomeTime()
        assertAmountAtPosition(amount = amount, position = 0)
    }

    private fun scrollToCurrency(currencyCode: String) {
        recyclerView.perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                        hasDescendant(withText(currencyCode.toUpperCase()))
                )
        )
    }

    private fun scrollToPosition(position: Int) {
        recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    private fun clickOnCurrencyItem(currencyCode: String) {
        recyclerView.perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(currencyCode.toUpperCase())), click()
                )
        )
    }

    private fun typeOnCurrencyItem(currencyCode: String, amount: Int) {
        fun typeText(amount: Int): ViewAction {
            return object : ViewAction {
                override fun perform(uiController: UiController?, view: View?) {
                    val amountEditText = (view as RelativeLayout).findViewById<AppCompatEditText>(R.id.amountEditText)
                    amountEditText.setText(amount.toString())
                }

                override fun getDescription(): String = ""
                override fun getConstraints(): Matcher<View> = isDisplayed()
            }
        }
        clickOnCurrencyItem(currencyCode)
        recyclerView.perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText(currencyCode.toUpperCase())),
                        typeText(amount)
                )
        )
    }

    private fun assertCurrencyCodeOnPosition(currencyCode: String, position: Int) {
        fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    val targetView = viewHolder?.itemView?.findViewById<View>(R.id.baseTextView)
                    return itemMatcher.matches(targetView)
                }

                override fun describeTo(description: Description?) {}
            }
        }

        recyclerView.check(matches(atPosition(position, withText(currencyCode.toUpperCase()))))
    }

    private fun assertAmountAtPosition(amount: Int, position: Int) {
        fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    val targetView = viewHolder?.itemView?.findViewById<View>(R.id.amountEditText)
                    return itemMatcher.matches(targetView)
                }

                override fun describeTo(description: Description?) {}
            }
        }
        scrollToPosition(position)
        recyclerView.check(matches(atPosition(position, withText(amount.toString()))))
    }

    private fun ViewInteraction.checkVisible() {
        this.check(matches(isDisplayed()))
    }

    private fun ViewInteraction.checkInvisible() {
        this.check(matches(not(isDisplayed())))
    }

    private fun waitSomeTime() {
        Thread.sleep(5000) // Should be replaced with idling :)
    }
}