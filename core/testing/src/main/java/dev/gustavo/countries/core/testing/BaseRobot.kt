package dev.gustavo.countries.core.testing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import dev.gustavo.countries.core.ui.components.SharedTestTags

@OptIn(ExperimentalTestApi::class)
abstract class BaseRobot(protected val composeTestRule: ComposeContentTestRule) {

    fun assertEmptyStateDisplayed(message: String) {
        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE).assertIsDisplayed()
        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(message)
    }

    fun assertErrorMessageDisplayed(message: String) {
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(message)
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).assertIsDisplayed()
    }

    fun assertTextDisplayed(text: String) {
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertTextDisplayedWithScroll(text: String) {
        composeTestRule.onNodeWithText(text).performScrollTo().assertIsDisplayed()
    }

    fun assertTagDisplayed(tag: String) {
        composeTestRule.onNodeWithTag(tag).assertIsDisplayed()
    }

    fun assertContentDescriptionDisplayed(description: String) {
        composeTestRule.onNode(hasContentDescription(description)).assertIsDisplayed()
    }

    fun clickOnTag(tag: String) {
        composeTestRule.onNodeWithTag(tag).performClick()
    }

    fun clickOnText(text: String) {
        composeTestRule.onNodeWithText(text).performClick()
    }

    fun clickOnTextWithScroll(text: String) {
        composeTestRule.onNodeWithText(text).performScrollTo().performClick()
    }

    fun enterTextIntoTag(tag: String, text: String) {
        composeTestRule.onNodeWithTag(tag).performTextInput(text)
    }

    fun waitUntilNodeExists(tag: String, timeout: Long = DEFAULT_TIMEOUT) {
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(tag), timeout)
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 5000L
    }
}
