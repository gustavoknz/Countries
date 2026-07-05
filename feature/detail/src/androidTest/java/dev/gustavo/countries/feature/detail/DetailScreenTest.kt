package dev.gustavo.countries.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import dev.gustavo.countries.core.testing.setCountriesContent
import dev.gustavo.countries.core.ui.components.SharedTestTags
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
        borders = persistentListOf("ARG"),
        currencies = UiText.DynamicString("Brazilian real"),
    )

    @Test
    fun givenLoadingState_whenScreenRendered_thenDisplaysSkeleton() {
        val viewState = DetailViewState.Loading()

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        composeTestRule.onNodeWithTag(DETAIL_SKELETON).assertIsDisplayed()
    }

    @Test
    fun givenLoadedState_whenScreenRendered_thenDisplaysCountryDetails() {
        val viewState = DetailViewState.Loaded(countryDetail)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        composeTestRule.onNodeWithTag(DETAIL_CONTENT).assertIsDisplayed()
        composeTestRule.onNodeWithTag(TOP_BAR_TITLE).assertTextEquals("Brazil")
        composeTestRule.onNodeWithTag(COMMON_NAME).assertTextEquals("Brazil")
        
        // Assert top info
        composeTestRule.onNodeWithText("Federative Republic of Brazil").assertIsDisplayed()
        
        // Scroll and assert secondary info
        composeTestRule.onNodeWithText("Brasília").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Americas").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("South America").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Portuguese").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("215,000,000").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Brazilian real").performScrollTo().assertIsDisplayed()
        
        // Check border chip
        composeTestRule.onNodeWithText("ARG").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun givenLoadedState_whenBackClicked_thenTriggersBackAction() {
        val onAction: (DetailAction) -> Unit = mockk(relaxed = true)
        val viewState = DetailViewState.Loaded(countryDetail)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithTag(BACK_BUTTON).performClick()

        verify { onAction(DetailAction.BackClicked) }
        confirmVerified(onAction)
    }

    @Test
    fun givenLoadedState_whenBorderClicked_thenTriggersBorderAction() {
        val onAction: (DetailAction) -> Unit = mockk(relaxed = true)
        val viewState = DetailViewState.Loaded(countryDetail)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithText("ARG").performScrollTo().performClick()

        verify { onAction(DetailAction.BorderClicked("ARG")) }
        confirmVerified(onAction)
    }

    @Test
    fun givenErrorState_whenScreenRendered_thenDisplaysErrorMessage() {
        val errorMessage = "An unexpected error occurred."
        val viewState = DetailViewState.Error(message = UiText.DynamicString(errorMessage))

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
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

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            DetailScreen(
                viewState = viewState,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON).performClick()

        verify { onAction(DetailAction.LoadDetail("BRA")) }
        confirmVerified(onAction)
    }
}
