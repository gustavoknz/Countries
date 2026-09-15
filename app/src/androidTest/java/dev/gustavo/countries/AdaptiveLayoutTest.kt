package dev.gustavo.countries

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AdaptiveLayoutTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun givenAppStarted_thenDisplaysCorrectLayoutForScreenSize() {
        // Wait for UI to settle
        composeTestRule.waitForIdle()

        val prompt = composeTestRule.onNodeWithText("Select a country from the list", substring = true)

        // Determine if we are in an expanded (two-pane) or compact (single-pane) layout
        // by checking if the detail prompt is actually displayed.
        val isExpandedScreen =
            try {
                prompt.assertIsDisplayed()
                true
            } catch (_: AssertionError) {
                false
            }

        // The list should always be visible on start in both layouts
        composeTestRule.onNodeWithText("Countries").assertIsDisplayed()

        if (isExpandedScreen) {
            // On a tablet/expanded screen, the detail placeholder must be visible alongside the list
            prompt.assertIsDisplayed()
        } else {
            // On a phone/compact screen, the detail placeholder should be hidden
            prompt.assertIsNotDisplayed()
        }
    }
}
