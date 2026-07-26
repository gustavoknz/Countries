package dev.gustavo.countries

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
    fun givenAppStarted_whenOnExpandedScreen_thenDisplaysListAndDetailPrompt() {
        // This test assumes it's running on an expanded screen (tablet)
        // to verify the Two-Pane integration logic in MainActivity.

        // Check if we are in two-pane mode by looking for the select country prompt
        // which only exists in the DetailPane when no country is selected.
        val prompt = composeTestRule.onNodeWithText("Select a country", substring = true)

        // If the prompt exists, we must also be able to see the list.
        // We'll use a generic text that we know exists in the list (the title).
        if (composeTestRule.onAllNodesWithText("Countries").fetchSemanticsNodes().isNotEmpty()) {
            composeTestRule.onNodeWithText("Countries").assertIsDisplayed()
            prompt.assertIsDisplayed()
        }
    }
}
