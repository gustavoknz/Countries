package dev.gustavo.countries.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.gustavo.countries.core.ui.components.SharedTestTags
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenLoadedState_whenScreenRendered_thenDisplaysCountryDetails() {
        val countryDetail = UiCountryDetail(
            cca3 = "BRA",
            commonName = "Brazil",
            officialName = "Federative Republic of Brazil",
            flagUrl = "",
            flagContentDescription = UiText.DynamicString("Brazil flag"),
            capital = UiText.DynamicString("Brasília"),
            independent = UiText.DynamicString("Yes"),
            region = UiText.DynamicString("Americas"),
            subregion = UiText.DynamicString("South America"),
            languages = UiText.DynamicString("Portuguese"),
            population = UiText.DynamicString("215,000,000"),
            bordersCount = UiText.DynamicString("1"),
            borders = listOf("ARG"),
            currencies = UiText.DynamicString("Brazilian real")
        )
        val viewState = DetailViewState.Loaded(countryDetail)

        composeTestRule.setContent {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailScreen(
                            viewState = viewState,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                            onAction = {}
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(COMMON_NAME)
            .assertIsDisplayed()
            .assertTextEquals("Brazil")
    }

    @Test
    fun givenErrorState_whenScreenRendered_thenDisplaysErrorMessage() {
        val errorMessage = "An unexpected error occurred."
        val viewState = DetailViewState.Error(message = UiText.DynamicString(errorMessage))

        composeTestRule.setContent {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailScreen(
                            viewState = viewState,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                            onAction = {}
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON)
            .assertIsDisplayed()
    }
}
