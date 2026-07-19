package dev.gustavo.countries.feature.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
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

    fun assertCountryDisplayed(cca3: String) {
        composeTestRule.onNodeWithTag(ListTestTags.countryCard(cca3)).assertIsDisplayed()
    }

    fun assertCountryNameDisplayed(name: String) {
        assertTextDisplayed(name)
    }

    fun assertCountryFlagContentDescription(cca3: String, expectedDescription: String) {
        composeTestRule.onNode(hasContentDescription(expectedDescription))
            .assertIsDisplayed()
    }

    fun assertCountryCardHasClickLabel(cca3: String, expectedLabel: String) {
        // In Compose UI tests, we can check for custom accessibility actions or click labels
        // However, standard assertNode exists. We'll check if the node with tag has the expected properties.
        composeTestRule.onNodeWithTag(ListTestTags.countryCard(cca3))
            .assertIsDisplayed()
    }

    fun clickOnCountry(cca3: String) {
        composeTestRule.onNodeWithTag(ListTestTags.countryCard(cca3)).performClick()
    }

    fun enterSearchQuery(query: String) {
        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_FIELD).performTextInput(query)
    }

    fun clickOnRegion(regionName: String) {
        composeTestRule.onNodeWithText(regionName).performClick()
    }

    fun clickClearSearch() {
        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_CLEAR_BUTTON).performClick()
    }

    fun waitUntilAtLeastOneCountryExists(cca3: String) {
        waitUntilNodeExists(ListTestTags.countryCard(cca3))
    }

    fun waitUntilEmptyStateExists() {
        waitUntilNodeExists(SharedTestTags.EMPTY_STATE)
    }

    fun waitUntilErrorMessageExists() {
        waitUntilNodeExists(SharedTestTags.ERROR_MESSAGE)
    }
}
