package com.jasvir.dogsapp.ui

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.jasvir.dogsapp.R
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class DogsFragmentTests {

    @get:Rule
    var mainActivityTestRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_spinner_displayed() {
        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.spBreed)
            )
        )
    }


    @Test
    fun test_spinner_click() {
        Thread.sleep(3000)

        onView(withId(R.id.spBreed))
            .perform(click())

        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Affenpinscher"))).perform(click())
        onView(withId(R.id.spBreed)).check(matches(withSpinnerText(containsString("Affenpinscher"))))

    }



    @Test
    fun test_recycler_view_displayed() {
        onView(
            Matchers.allOf(
                isDisplayed(),
                withId(R.id.dogsRecyclerView)
            )
        )
    }


    @Test
    fun test_recycler_view_count() {
        var activity: Activity? = null

        mainActivityTestRule.scenario.onActivity {
            activity = it
        }

        val recyclerView: RecyclerView = activity!!.findViewById(R.id.dogsRecyclerView)
        onView(Matchers.allOf(isDisplayed(), withId(R.id.dogsRecyclerView)))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(recyclerView.adapter!!.itemCount - 1))

    }



}