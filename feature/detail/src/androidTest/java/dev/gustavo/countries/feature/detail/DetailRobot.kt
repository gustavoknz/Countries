package dev.gustavo.countries.feature.detail

import androidx.compose.ui.test.ExperimentalTestApi
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

    @OptIn(ExperimentalTestApi::class)
    fun assertSkeletonDisplayed() {
        composeTestRule.onNodeWithTag(DETAIL_SKELETON).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertContentDisplayed() {
        composeTestRule.onNodeWithTag(DETAIL_CONTENT).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertTopBarTitle(title: String) {
        composeTestRule.onNodeWithTag(TOP_BAR_TITLE).assertTextEquals(title)
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertCommonName(name: String) {
        composeTestRule.onNodeWithTag(COMMON_NAME).assertTextEquals(name)
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertTextDisplayedWithScroll(text: String) {
        composeTestRule.onNodeWithText(text).performScrollTo().assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickBack() {
        composeTestRule.onNodeWithTag(BACK_BUTTON).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickOnBorder(cca3: String) {
        composeTestRule.onNodeWithText(cca3).performScrollTo().performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickRetry() {
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).performClick()
    }
}
