package com.example.pokemon


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        Thread.sleep(3000)
        val materialButton = onView(
            allOf(
                withId(R.id.btnRegister), withText("Sign Up"),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.etName),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("aa"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.etEmail),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("aa"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.et_password),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("aa"), closeSoftKeyboard())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.etConfirmPassword),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("aa"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnRegister), withText("Sign Up"),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.etUser),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("aa"), closeSoftKeyboard())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.et_password),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("aa"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnLogin), withText("Login"),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
    }

    /*private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }*/
}
