package dev.gustavo.countries.feature.list

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.gustavo.countries.core.testing.BaseRobot
import dev.gustavo.countries.core.ui.components.SharedTestTags

fun listRobot(composeTestRule: ComposeContentTestRule, func: ListRobot.() -> Unit) =
    ListRobot(composeTestRule).apply(func)

class ListRobot(composeTestRule: ComposeContentTestRule) : BaseRobot(composeTestRule) {

    @OptIn(ExperimentalTestApi::class)
    fun assertCountryDisplayed(cca3: String) {
        composeTestRule.onNodeWithTag(ListTestTags.countryCard(cca3)).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    fun assertCountryNameDisplayed(name: String) {
        assertTextDisplayed(name)
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickOnCountry(cca3: String) {
        composeTestRule.onNodeWithTag(ListTestTags.countryCard(cca3)).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun enterSearchQuery(query: String) {
        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_FIELD).performTextInput(query)
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickOnRegion(regionName: String) {
        composeTestRule.onNodeWithText(regionName).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun clickClearSearch() {
        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_CLEAR_BUTTON).performClick()
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilAtLeastOneCountryExists(cca3: String) {
        waitUntilNodeExists(ListTestTags.countryCard(cca3))
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilEmptyStateExists() {
        waitUntilNodeExists(SharedTestTags.EMPTY_STATE)
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitUntilErrorMessageExists() {
        waitUntilNodeExists(SharedTestTags.ERROR_MESSAGE)
    }
}
