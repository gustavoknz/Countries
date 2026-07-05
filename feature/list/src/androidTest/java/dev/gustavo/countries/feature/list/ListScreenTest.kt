package dev.gustavo.countries.feature.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.gustavo.countries.core.testing.setCountriesContent
import dev.gustavo.countries.core.ui.components.SharedTestTags
import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenSuccessState_whenScreenRendered_thenDisplaysCountryList() {
        val countries = listOf(
            UiCountry("BRA", "Brazil", "Brasília", "", "Americas", true),
            UiCountry("USA", "United States", "Washington D.C.", "", "Americas", true)
        )
        val pagingData = PagingData.from(
            data = countries,
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            )
        )
        val countriesFlow = flowOf(pagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        val braTag = ListTestTags.countryCard("BRA")
        val usaTag = ListTestTags.countryCard("USA")

        // Wait for the specific nodes to appear in the hierarchy
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(braTag), 5000L)

        composeTestRule.onNodeWithTag(braTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(usaTag).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenErrorState_whenScreenRendered_thenDisplaysErrorMessage() {
        val pagingData = PagingData.empty<UiCountry>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(RuntimeException("API Error")),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            )
        )
        val countriesFlow = flowOf(pagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        // Wait for the error message to appear
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(SharedTestTags.ERROR_MESSAGE), 5000L)

        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals("An unexpected error occurred.")
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON)
            .assertIsDisplayed()
    }
}
