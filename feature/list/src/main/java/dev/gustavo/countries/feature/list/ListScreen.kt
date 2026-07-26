package dev.gustavo.countries.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.common.toDataError
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.core.ui.util.toUiText
import dev.gustavo.countries.feature.list.components.CountriesGrid
import dev.gustavo.countries.feature.list.components.ListContent
import dev.gustavo.countries.feature.list.components.ModernSearchBar
import dev.gustavo.countries.feature.list.components.RegionFilterChips
import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
fun ListRoute(
    onCountryClick: (String, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val countries = viewModel.countries.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedRegion by viewModel.selectedRegion.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ListEvent.NavigateToDetail -> onCountryClick(event.countryCode, event.flagUrl)
            }
        }
    }

    ListScreen(
        countries = countries,
        searchQuery = searchQuery,
        selectedRegion = selectedRegion,
        isOffline = isOffline,
        snackbarHostState = snackbarHostState,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onAction = { action ->
            when (action) {
                is ListAction.CountryClicked -> {
                    onCountryClick(action.countryCode, action.flagUrl)
                }
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
fun ListScreen(
    countries: LazyPagingItems<UiCountry>,
    searchQuery: String,
    selectedRegion: Region?,
    isOffline: Boolean,
    snackbarHostState: SnackbarHostState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    onAction: (ListAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    val refreshError = countries.loadState.refresh as? LoadState.Error
    val appendError = countries.loadState.append as? LoadState.Error
    val error = refreshError ?: appendError

    LaunchedEffect(error) {
        if (error != null && countries.itemCount > 0) {
            val dataError = error.error.toDataError()
            snackbarHostState.showSnackbar(
                message = dataError.toUiText().asString(context),
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = Dimens.PaddingSmall),
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.list_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                )

                ModernSearchBar(
                    searchQuery = searchQuery,
                    isOffline = isOffline,
                    focusRequester = focusRequester,
                    onSearchQueryChanged = { onAction(ListAction.SearchQueryChanged(it)) },
                    onSearchClicked = { keyboardController?.hide() },
                )

                RegionFilterChips(
                    selectedRegion = selectedRegion,
                    onRegionSelected = { onAction(ListAction.RegionSelected(it)) },
                )
            }
        },
    ) { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing = countries.loadState.refresh is LoadState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { countries.refresh() },
            state = pullToRefreshState,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            val refreshState = countries.loadState.refresh
            val sourceRefreshState = countries.loadState.source.refresh
            val onRetry = remember(countries) { { countries.retry() } }

            val showEmptyState = (sourceRefreshState is LoadState.NotLoading) && countries.itemCount == 0
            val hasItems = countries.itemCount > 0

            ListContent(
                isLoading = refreshState is LoadState.Loading,
                showEmptyState = showEmptyState,
                error = if (!hasItems) (refreshState as? LoadState.Error)?.error?.toDataError() else null,
                searchQuery = searchQuery,
                selectedRegion = selectedRegion,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize(),
            ) {
                CountriesGrid(
                    countries = countries,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onCountryClick = { countryCode, flagUrl ->
                        onAction(ListAction.CountryClicked(countryCode, flagUrl))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreview() {
    val fakeData =
        remember {
            flowOf(
                PagingData.from(
                    listOf(
                        UiCountry("BRA", "Brazil", "Brasília", "", "Americas", true),
                        UiCountry("GRL", "Greenland", "Nuuk", "", "Americas", false),
                    ),
                    sourceLoadStates =
                        LoadStates(
                            refresh = LoadState.NotLoading(false),
                            prepend = LoadState.NotLoading(false),
                            append = LoadState.NotLoading(false),
                        ),
                ),
            )
        }
    CountriesTheme {
        SharedTransitionLayout {
            @Suppress("UnusedContentLambdaTargetStateParameter")
            AnimatedContent(targetState = Unit, label = "preview") {
                ListScreen(
                    countries = fakeData.collectAsLazyPagingItems(),
                    searchQuery = "",
                    selectedRegion = null,
                    isOffline = false,
                    snackbarHostState = remember { SnackbarHostState() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    onAction = {},
                )
            }
        }
    }
}
