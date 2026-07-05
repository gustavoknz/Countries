package dev.gustavo.countries.feature.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.testing.setCountriesContent
import dev.gustavo.countries.core.ui.components.SharedTestTags
import dev.gustavo.countries.feature.list.model.UiCountry
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val countries = listOf(
        UiCountry("BRA", "Brazil", "Brasília", "flag_bra", "Americas", true),
        UiCountry("USA", "United States", "Washington D.C.", "flag_usa", "Americas", true)
    )

    private val successPagingData = PagingData.from(
        data = countries,
        sourceLoadStates = LoadStates(
            refresh = LoadState.NotLoading(false),
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false)
        )
    )

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenSuccessState_whenScreenRendered_thenDisplaysCountryList() {
        val countriesFlow = flowOf(successPagingData)

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

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(braTag), 5000L)

        composeTestRule.onNodeWithTag(braTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(usaTag).assertIsDisplayed()
        composeTestRule.onNodeWithText("Brazil").assertIsDisplayed()
        composeTestRule.onNodeWithText("United States").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenEmptySearchState_whenScreenRendered_thenDisplaysNoSearchResultsMessage() {
        val searchQuery = "NonExistent"
        val pagingData = PagingData.empty<UiCountry>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        val countriesFlow = flowOf(pagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = searchQuery,
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(SharedTestTags.EMPTY_STATE), 5000L)

        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals("No results found for \"$searchQuery\"")
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenEmptyRegionState_whenScreenRendered_thenDisplaysNoRegionResultsMessage() {
        val selectedRegion = Region.EUROPE
        val pagingData = PagingData.empty<UiCountry>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(true),
                append = LoadState.NotLoading(true)
            )
        )
        val countriesFlow = flowOf(pagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = selectedRegion,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = {}
            )
        }

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(SharedTestTags.EMPTY_STATE), 5000L)

        composeTestRule.onNodeWithTag(SharedTestTags.EMPTY_STATE_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals("No countries found in Europe")
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

        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(SharedTestTags.ERROR_MESSAGE), 5000L)

        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals("An unexpected error occurred.")
        composeTestRule.onNodeWithTag(SharedTestTags.ERROR_RETRY_BUTTON)
            .assertIsDisplayed()
    }

    @Test
    fun givenCountryList_whenCountryClicked_thenTriggersAction() {
        val onAction: (ListAction) -> Unit = mockk(relaxed = true)
        val countriesFlow = flowOf(successPagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithTag(ListTestTags.countryCard("BRA")).performClick()

        verify { onAction(ListAction.CountryClicked("BRA", "flag_bra")) }
    }

    @Test
    fun whenSearching_thenTriggersAction() {
        val onAction: (ListAction) -> Unit = mockk(relaxed = true)
        val countriesFlow = flowOf(successPagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_FIELD).performTextInput("arg")

        verify { onAction(ListAction.SearchQueryChanged("arg")) }
    }

    @Test
    fun whenRegionSelected_thenTriggersAction() {
        val onAction: (ListAction) -> Unit = mockk(relaxed = true)
        val countriesFlow = flowOf(successPagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithText("Africa").performClick()

        verify { onAction(ListAction.RegionSelected(Region.AFRICA)) }
    }

    @Test
    fun whenSearchQueryPresent_andClearClicked_thenTriggersClearAction() {
        val onAction: (ListAction) -> Unit = mockk(relaxed = true)
        val countriesFlow = flowOf(successPagingData)

        composeTestRule.setCountriesContent { sharedTransitionScope, animatedContentScope ->
            ListScreen(
                countries = countriesFlow.collectAsLazyPagingItems(),
                searchQuery = "bra",
                selectedRegion = null,
                isOffline = false,
                snackbarHostState = remember { SnackbarHostState() },
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction
            )
        }

        composeTestRule.onNodeWithTag(ListTestTags.SEARCH_CLEAR_BUTTON).performClick()

        verify { onAction(ListAction.SearchQueryChanged("")) }
    }
}
