package dev.gustavo.countries.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dev.gustavo.countries.core.common.toDataError
import dev.gustavo.countries.core.ui.components.EmptyState
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.SkeletonItem
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.core.ui.util.toUiText
import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

object ListTestTags {
    const val SEARCH_FIELD = "list_search_field"
    const val SEARCH_CLEAR_BUTTON = "list_search_clear_button"
    fun countryCard(cca3: String) = "country_card_$cca3"
}

@Composable
fun ListRoute(
    onCountryClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: ListViewModel = hiltViewModel()
) {
    val countries = viewModel.countries.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ListEvent.NavigateToDetail -> onCountryClick(event.cca3)
            }
        }
    }

    ListScreen(
        countries = countries,
        searchQuery = searchQuery,
        isOffline = isOffline,
        snackbarHostState = snackbarHostState,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onAction = viewModel::onAction
    )
}

@Composable
fun ListScreen(
    countries: LazyPagingItems<UiCountry>,
    searchQuery: String,
    isOffline: Boolean,
    snackbarHostState: SnackbarHostState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onAction: (ListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.list_title),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                TextField(
                    value = searchQuery,
                    onValueChange = { onAction(ListAction.SearchQueryChanged(it)) },
                    placeholder = { Text(stringResource(R.string.list_search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.list_search_icon_description)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onAction(ListAction.SearchQueryChanged("")) },
                                modifier = Modifier.testTag(ListTestTags.SEARCH_CLEAR_BUTTON)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.list_search_clear_description)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.PaddingExtraLarge, vertical = Dimens.PaddingMedium)
                        .border(
                            border = BorderStroke(
                                width = 2.dp,
                                color = if (isOffline) MaterialTheme.colorScheme.error else Color.Transparent
                            ),
                            shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
                        )
                        .testTag(ListTestTags.SEARCH_FIELD)
                )
            }
        }
    ) { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()
        val isRefreshing = countries.loadState.refresh is LoadState.Loading

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { countries.refresh() },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val refreshState = countries.loadState.refresh
            when {
                refreshState is LoadState.Loading && countries.itemCount == 0 -> {
                    LoadingSkeletonGrid()
                }

                refreshState is LoadState.Error && countries.itemCount == 0 -> {
                    ErrorState(
                        message = refreshState.error.toDataError().toUiText().asString(),
                        retryLabel = stringResource(R.string.list_error_retry),
                        onRetry = { countries.retry() }
                    )
                }

                refreshState is LoadState.NotLoading && countries.itemCount == 0 && refreshState.endOfPaginationReached -> {
                    val emptyMessage = if (searchQuery.isNotBlank()) {
                        stringResource(R.string.list_empty_search_result, searchQuery)
                    } else {
                        stringResource(R.string.list_empty_result)
                    }
                    EmptyState(message = emptyMessage)
                }

                else -> {
                    CountriesGrid(
                        countries = countries,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        onCountryClick = { onAction(ListAction.CountryClicked(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CountriesGrid(
    countries: LazyPagingItems<UiCountry>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = Dimens.PaddingLarge,
            end = Dimens.PaddingLarge,
            top = Dimens.PaddingMedium,
            bottom = Dimens.PaddingMedium
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        modifier = modifier.fillMaxSize()
    ) {
        items(
            count = countries.itemCount,
            key = countries.itemKey { it.cca3 }
        ) { index ->
            val country = countries[index]
            if (country != null) {
                CountryCard(
                    country = country,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onClick = { onCountryClick(country.cca3) }
                )
            } else {
                CountryCardSkeleton()
            }
        }

        if (countries.loadState.append is LoadState.Loading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.PaddingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun LoadingSkeletonGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = Dimens.PaddingLarge,
            end = Dimens.PaddingLarge,
            top = Dimens.PaddingMedium,
            bottom = Dimens.PaddingMedium
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        userScrollEnabled = false,
        modifier = modifier.fillMaxSize()
    ) {
        items(6) {
            CountryCardSkeleton()
        }
    }
}

@Composable
private fun CountryCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationMedium)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.FlagImageHeightMedium)
            )
            Column(modifier = Modifier.padding(top = Dimens.PaddingMedium)) {
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                )
                Spacer(Modifier.height(Dimens.PaddingSmall))
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(12.dp)
                )
                Spacer(Modifier.height(Dimens.PaddingSmall))
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(10.dp)
                )
            }
        }
    }
}

@Composable
private fun CountryCard(
    country: UiCountry,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val border = if (country.independent) {
        null
    } else {
        BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.error
        )
    }

    Card(
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag(ListTestTags.countryCard(country.cca3)),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationMedium),
        border = border
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            with(sharedTransitionScope) {
                FlagImage(
                    url = country.flagUrl,
                    contentDescription = stringResource(R.string.list_flag_content_description, country.commonName),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState(key = "flag-${country.cca3}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                        .fillMaxWidth()
                        .height(Dimens.FlagImageHeightMedium)
                )
            }
            Column(modifier = Modifier.padding(top = Dimens.PaddingMedium)) {
                with(sharedTransitionScope) {
                    Text(
                        text = country.commonName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.sharedBounds(
                            sharedTransitionScope.rememberSharedContentState(key = "name-${country.cca3}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                }
                if (country.capital.isNotBlank()) {
                    Text(
                        text = country.capital,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = country.region,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (!country.independent) {
                    Text(
                        text = stringResource(R.string.list_not_independent),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = Dimens.PaddingSmall)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenPreview() {
    val fakeData = remember {
        flowOf(
            PagingData.from(
                listOf(
                    UiCountry("BRA", "Brazil", "Brasília", "", "Americas", true),
                    UiCountry("GRL", "Greenland", "Nuuk", "", "Americas", false)
                ),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false)
                )
            )
        )
    }
    CountriesTheme {
        SharedTransitionLayout {
            @Suppress("UnusedContentLambdaTargetStateParameter")
            AnimatedContent(targetState = Unit, label = "preview") {
                ListScreen(
                    countries = fakeData.collectAsLazyPagingItems(),
                    searchQuery = "",
                    isOffline = false,
                    snackbarHostState = remember { SnackbarHostState() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    onAction = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListScreenLoadingPreview() {
    val fakeLoadingData = flowOf(
        PagingData.empty<UiCountry>(
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            )
        )
    )
    CountriesTheme {
        SharedTransitionLayout {
            @Suppress("UnusedContentLambdaTargetStateParameter")
            AnimatedContent(targetState = Unit, label = "preview") {
                ListScreen(
                    countries = fakeLoadingData.collectAsLazyPagingItems(),
                    searchQuery = "",
                    isOffline = false,
                    snackbarHostState = remember { SnackbarHostState() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    onAction = {}
                )
            }
        }
    }
}
