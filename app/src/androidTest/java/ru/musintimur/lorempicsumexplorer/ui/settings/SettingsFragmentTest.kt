package ru.musintimur.lorempicsumexplorer.ui.settings

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.musintimur.lorempicsumexplorer.R


class SettingsFragmentAndroidTest {

    private val bundle = Bundle()
    private lateinit var scenario: FragmentScenario<SettingsFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer<SettingsFragment>(bundle, R.style.Theme_AppCompat)
    }


    @Test
    fun testDefaultSettings() {
        onView(withId(R.id.editTextPhotoWidth)).check(matches(withText(600.toString())))
        onView(withId(R.id.editTextPhotoHeight)).check(matches(withText(600.toString())))
        onView(withId(R.id.editTextPerPage)).check(matches(withText(20.toString())))
        onView(withId(R.id.editTextInitialLoad)).check(matches(withText(40.toString())))
    }

    @Test
    fun testWidthValidation() {
        var isValid = true
        onView(withId(R.id.editTextPhotoWidth)).perform(ViewActions.clearText())
        with(scenario) {
            onFragment { isValid = it.validateInput() }
        }
        Assert.assertEquals(false, isValid)
    }

    @Test
    fun testHeightValidation() {
        var isValid = true
        onView(withId(R.id.editTextPhotoHeight))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("5000"))
        with(scenario) {
            onFragment { isValid = it.validateInput() }
        }
        Assert.assertEquals(false, isValid)
    }

    @Test
    fun testPageSizeValidation() {
        var isValid = true
        onView(withId(R.id.editTextPerPage))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText("0"))
        with(scenario) {
            onFragment { isValid = it.validateInput() }
        }
        Assert.assertEquals(false, isValid)
    }

    @Test
    fun testInitialPageSizeValidation() {
        var isValid = true
        onView(withId(R.id.editTextInitialLoad))
            .perform(ViewActions.clearText())
            .perform(ViewActions.typeText(""))
        with(scenario) {
            onFragment { isValid = it.validateInput() }
        }
        Assert.assertEquals(false, isValid)
    }
}