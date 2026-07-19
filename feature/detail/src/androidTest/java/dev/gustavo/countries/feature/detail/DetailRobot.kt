package dev.gustavo.countries.feature.detail

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import dev.gustavo.countries.core.testing.BaseRobot
import dev.gustavo.countries.core.ui.components.SharedTestTags

fun detailRobot(composeTestRule: ComposeContentTestRule, func: DetailRobot.() -> Unit) =
    DetailRobot(composeTestRule).apply(func)

class DetailRobot(composeTestRule: ComposeContentTestRule) : BaseRobot(composeTestRule) {

    fun assertSkeletonDisplayed() {
        assertTagDisplayed(DETAIL_SKELETON)
    }

    fun assertContentDisplayed() {
        assertTagDisplayed(DETAIL_CONTENT)
    }

    fun assertTopBarTitle(title: String) {
        composeTestRule.onNodeWithTag(TOP_BAR_TITLE).assertTextEquals(title)
    }

    fun assertCommonName(name: String) {
        composeTestRule.onNodeWithTag(COMMON_NAME).assertTextEquals(name)
    }

    fun clickBack() {
        clickOnTag(BACK_BUTTON)
    }

    fun clickOnBorder(cca3: String) {
        clickOnTextWithScroll(cca3)
    }

    fun clickRetry() {
        clickOnTag(SharedTestTags.ERROR_RETRY_BUTTON)
    }
}
