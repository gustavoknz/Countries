package dev.gustavo.countries.feature.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.testing.setCountriesContent
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

        listRobot(composeTestRule) {
            waitUntilAtLeastOneCountryExists("BRA")
            assertCountryDisplayed("BRA")
            assertCountryDisplayed("USA")
            assertCountryNameDisplayed("Brazil")
            assertCountryNameDisplayed("United States")
        }
    }

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

        listRobot(composeTestRule) {
            waitUntilEmptyStateExists()
            assertEmptyStateDisplayed("No results found for \"$searchQuery\"")
        }
    }

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

        listRobot(composeTestRule) {
            waitUntilEmptyStateExists()
            assertEmptyStateDisplayed("No countries found in Europe")
        }
    }

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

        listRobot(composeTestRule) {
            waitUntilErrorMessageExists()
            assertErrorMessageDisplayed("An unexpected error occurred.")
        }
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

        listRobot(composeTestRule) {
            clickOnCountry("BRA")
        }

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

        listRobot(composeTestRule) {
            enterSearchQuery("arg")
        }

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

        listRobot(composeTestRule) {
            clickOnRegion("Africa")
        }

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

        listRobot(composeTestRule) {
            clickClearSearch()
        }

        verify { onAction(ListAction.SearchQueryChanged("")) }
    }

    @Test
    fun givenCountryCard_whenDisplayed_thenHasCorrectAccessibilitySemantics() {
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

        listRobot(composeTestRule) {
            waitUntilAtLeastOneCountryExists("BRA")
            assertCountryFlagContentDescription("BRA", "Brazil flag")
        }
    }
}
