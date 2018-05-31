package com.target.dealbrowserpoc.dealbrowser

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Test
import android.support.v7.widget.RecyclerView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.view.View
import junit.framework.Assert.assertTrue


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java)

    @Test
    fun verifyResultsReturned() {
        onView(withId(R.id.dealsRecyclerView)).check(RecyclerViewItemCountAssertion(0))
    }

}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter

        assertTrue("Adapter should not be null", adapter != null)
        assertTrue("Adapter item count should be greater than 0", adapter.itemCount > expectedCount)

    }
}