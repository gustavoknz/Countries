package dev.gustavo.countries.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dev.gustavo.countries.core.testing.BaseRobot
import dev.gustavo.countries.core.ui.components.SharedTestTags

fun detailRobot(composeTestRule: ComposeContentTestRule, func: DetailRobot.() -> Unit) =
    DetailRobot(composeTestRule).apply(func)

class DetailRobot(composeTestRule: ComposeContentTestRule) : BaseRobot(composeTestRule) {

    fun assertSkeletonDisplayed() {
        composeTestRule.onNodeWithTag(DETAIL_SKELETON).assertIsDisplayed()
    }

    fun assertContentDisplayed() {
        composeTestRule.onNodeWithTag(DETAIL_CONTENT).assertIsDisplayed()
    }

    fun assertTopBarTitle(title: String) {
        composeTestRule.onNodeWithTag(TOP_BAR_TITLE).assertTextEquals(title)
    }

    fun assertCommonName(name: String) {
        composeTestRule.onNodeWithTag(COMMON_NAME).assertTextEquals(name)
    }

    fun assertTextDisplayedWithScroll(text: String) {
        composeTestRule.onNodeWithText(text).performScrollTo().assertIsDisplayed()
    }

    fun clickBack() {
        composeTestRule.onNodeWithTag(BACK_BUTTON).performClick()
    }

    fun clickOnBorder(cca3: String) {
        composeTestRule.onNodeWithText(cca3).performScrollTo().performClick()
    }

    fun clickRetry() {
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).performClick()
    }
}
