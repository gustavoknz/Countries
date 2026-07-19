package dev.gustavo.countries.feature.list

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import dev.gustavo.countries.core.testing.BaseRobot
import dev.gustavo.countries.core.ui.components.SharedTestTags

fun listRobot(composeTestRule: ComposeContentTestRule, func: ListRobot.() -> Unit) =
    ListRobot(composeTestRule).apply(func)

class ListRobot(composeTestRule: ComposeContentTestRule) : BaseRobot(composeTestRule) {

    fun assertCountryDisplayed(cca3: String) {
        assertTagDisplayed(ListTestTags.countryCard(cca3))
    }

    fun assertCountryNameDisplayed(name: String) {
        assertTextDisplayed(name)
    }

    fun assertCountryFlagContentDescription(expectedDescription: String) {
        assertContentDescriptionDisplayed(expectedDescription)
    }

    fun clickOnCountry(cca3: String) {
        clickOnTag(ListTestTags.countryCard(cca3))
    }

    fun enterSearchQuery(query: String) {
        enterTextIntoTag(ListTestTags.SEARCH_FIELD, query)
    }

    fun clickOnRegion(regionName: String) {
        clickOnText(regionName)
    }

    fun clickClearSearch() {
        clickOnTag(ListTestTags.SEARCH_CLEAR_BUTTON)
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
