package dev.gustavo.countries.core.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import dev.gustavo.countries.core.ui.components.SharedTestTags

abstract class BaseRobot(protected val composeTestRule: ComposeContentTestRule) {

    @OptIn(ExperimentalTestApi::class)
    fun assertEmptyStateDisplayed(message: String) {
        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(message)
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertErrorMessageDisplayed(message: String) {
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(message)
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertTextDisplayed(text: String) {
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilNodeExists(tag: String, timeout: Long = 5000L) {
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(tag), timeout)
    }
}
