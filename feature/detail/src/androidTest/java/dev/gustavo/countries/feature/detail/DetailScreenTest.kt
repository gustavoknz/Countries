package dev.gustavo.countries.feature.detail

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.gustavo.countries.core.ui.components.SharedTestTags
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val countryDetail = UiCountryDetail(
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
        currencies = UiText.DynamicString("Brazilian real"),
    )

    @Test
    fun givenLoadingState_whenScreenRendered_thenDisplaysSkeleton() {
        val viewState = DetailViewState.Loading()

        composeTestRule.setContent {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailScreen(
                            viewState = viewState,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                        ) {}
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(DETAIL_SKELETON).assertIsDisplayed()
    }

    @Test
    fun givenLoadedState_whenScreenRendered_thenDisplaysCountryDetails() {
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
                        ) {}
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(DETAIL_CONTENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TOP_BAR_TITLE).assertTextEquals("Brazil")
        composeTestRule.onNodeWithTag(COMMON_NAME).assertTextEquals("Brazil")
        composeTestRule.onNodeWithText("Federative Republic of Brazil").assertIsDisplayed()
        composeTestRule.onNodeWithText("Brasília").assertIsDisplayed()
        composeTestRule.onNodeWithText("Americas").assertIsDisplayed()
        composeTestRule.onNodeWithText("South America").assertIsDisplayed()
        composeTestRule.onNodeWithText("Portuguese").assertIsDisplayed()
        composeTestRule.onNodeWithText("215,000,000").assertIsDisplayed()
        composeTestRule.onNodeWithText("Brazilian real").assertIsDisplayed()
        
        // Check border chip
        composeTestRule.onNodeWithText("ARG").assertIsDisplayed()
    }

    @Test
    fun givenLoadedState_whenBackClicked_thenTriggersBackAction() {
        val onAction: (DetailAction) -> Unit = mockk(relaxed = true)
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
                            onAction = onAction
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(BACK_BUTTON).performClick()

        verify { onAction(DetailAction.BackClicked) }
        confirmVerified(onAction)
    }

    @Test
    fun givenLoadedState_whenBorderClicked_thenTriggersBorderAction() {
        val onAction: (DetailAction) -> Unit = mockk(relaxed = true)
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
                            onAction = onAction
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("ARG").performClick()

        verify { onAction(DetailAction.BorderClicked("ARG")) }
        confirmVerified(onAction)
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
                        ) {}
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

    @Test
    fun givenErrorState_whenRetryClicked_thenTriggersLoadAction() {
        val onAction: (DetailAction) -> Unit = mockk(relaxed = true)
        val viewState = DetailViewState.Error(
            message = UiText.DynamicString("Error"),
            countryCode = "BRA"
        )

        composeTestRule.setContent {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailScreen(
                            viewState = viewState,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                            onAction = onAction
                        )
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).performClick()

        verify { onAction(DetailAction.LoadDetail("BRA")) }
        confirmVerified(onAction)
    }
}
