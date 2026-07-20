package dev.gustavo.countries.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
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
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.gustavo.countries.core.common.DataError
import dev.gustavo.countries.core.common.Region
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

@Composable
fun ListRoute(
    onCountryClick: (String, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: ListViewModel = hiltViewModel()
) {
    val countries = viewModel.countries.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedRegion by viewModel.selectedRegion.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ListEvent.NavigateToDetail -> onCountryClick(event.cca3, event.flagUrl)
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
        onAction = viewModel::onAction
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
    animatedContentScope: AnimatedContentScope,
    onAction: (ListAction) -> Unit
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
                message = dataError.toUiText().asString(context)
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(bottom = Dimens.PaddingSmall)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.list_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                ModernSearchBar(
                    searchQuery = searchQuery,
                    isOffline = isOffline,
                    focusRequester = focusRequester,
                    onSearchQueryChanged = { onAction(ListAction.SearchQueryChanged(it)) },
                    onSearchClicked = { keyboardController?.hide() }
                )

                RegionFilterChips(
                    selectedRegion = selectedRegion,
                    onRegionSelected = { onAction(ListAction.RegionSelected(it)) }
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
                modifier = Modifier.fillMaxSize()
            ) {
                CountriesGrid(
                    countries = countries,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onCountryClick = { cca3, flagUrl ->
                        onAction(ListAction.CountryClicked(cca3, flagUrl))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ModernSearchBar(
    searchQuery: String,
    isOffline: Boolean,
    focusRequester: FocusRequester,
    onSearchQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = {
            Text(
                text = stringResource(R.string.list_search_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                        focusRequester.requestFocus()
                    },
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
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingExtraLarge)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isOffline) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
                ),
                shape = RoundedCornerShape(Dimens.CornerRadiusLarge)
            )
            .focusRequester(focusRequester)
            .testTag(ListTestTags.SEARCH_FIELD)
    )
}

@Composable
private fun RegionFilterChips(
    selectedRegion: Region?,
    onRegionSelected: (Region?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.PaddingExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingSmall)
    ) {
        item {
            FilterChip(
                selected = selectedRegion == null,
                onClick = { onRegionSelected(null) },
                label = { Text(stringResource(R.string.list_filter_all)) },
                leadingIcon = if (selectedRegion == null) {
                    {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.IconSizeSmall)
                        )
                    }
                } else {
                    null
                },
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
            )
        }
        items(Region.entries) { region ->
            FilterChip(
                selected = selectedRegion == region,
                onClick = {
                    if (selectedRegion == region) onRegionSelected(null) else onRegionSelected(region)
                },
                label = { Text(region.apiValue) },
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@Composable
private fun ListContent(
    isLoading: Boolean,
    showEmptyState: Boolean,
    error: DataError?,
    searchQuery: String,
    selectedRegion: Region?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when {
        isLoading && !showEmptyState -> {
            LoadingSkeletonGrid(modifier)
        }

        error != null -> {
            ErrorState(
                message = error.toUiText().asString(),
                retryLabel = stringResource(R.string.list_error_retry),
                onRetry = onRetry,
                modifier = modifier
            )
        }

        showEmptyState -> {
            val emptyMessage = when {
                searchQuery.isNotBlank() && selectedRegion != null -> {
                    stringResource(R.string.list_empty_search_with_region_result, searchQuery, selectedRegion.apiValue)
                }

                searchQuery.isNotBlank() -> {
                    stringResource(R.string.list_empty_search_result, searchQuery)
                }

                selectedRegion != null -> {
                    stringResource(R.string.list_empty_region_result, selectedRegion.apiValue)
                }

                else -> {
                    stringResource(R.string.list_empty_result)
                }
            }
            EmptyState(message = emptyMessage, modifier = modifier)
        }

        else -> {
            Box(modifier = modifier) {
                content()
            }
        }
    }
}

@Composable
private fun CountriesGrid(
    countries: LazyPagingItems<UiCountry>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCountryClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(COUNTRIES_GRID_COLUMNS),
        contentPadding = PaddingValues(
            start = Dimens.PaddingLarge,
            end = Dimens.PaddingLarge,
            top = Dimens.PaddingMedium,
            bottom = Dimens.PaddingLarge
        ),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
        modifier = modifier
    ) {
        items(
            count = countries.itemCount,
            key = countries.itemKey { it.cca3 },
            contentType = countries.itemContentType { "country" }
        ) { index ->
            val country = countries[index]
            if (country != null) {
                CountryCard(
                    country = country,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onClick = { onCountryClick(country.cca3, country.flagUrl) }
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
                    CircularProgressIndicator(
                        strokeWidth = Dimens.ProgressIndicatorStrokeWidth,
                        modifier = Modifier.size(Dimens.ProgressIndicatorSize)
                    )
                }
            }
        }
    }
}

@Composable
internal fun LoadingSkeletonGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(COUNTRIES_GRID_COLUMNS),
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
        items(SKELETON_ITEM_COUNT) {
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
        Column {
            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(FLAG_ASPECT_RATIO)
            )
            Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(CARD_TITLE_SKELETON_WIDTH_FRACTION)
                        .height(CARD_TITLE_SKELETON_HEIGHT)
                )
                Spacer(Modifier.height(Dimens.PaddingSmall))
                SkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(CARD_SUBTITLE_SKELETON_WIDTH_FRACTION)
                        .height(CARD_SUBTITLE_SKELETON_HEIGHT)
                )
            }
        }
    }
}

@Composable
internal fun CountryCard(
    country: UiCountry,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clickLabel = stringResource(R.string.list_card_click_label, country.commonName)
    Card(
        modifier = modifier
            .testTag(ListTestTags.countryCard(country.cca3))
            .semantics(mergeDescendants = true) {
                role = Role.Button
                onClick(label = clickLabel) {
                    onClick()
                    true
                }
            }
            .clickable(
                onClickLabel = clickLabel,
                onClick = onClick
            ),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
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
                        .aspectRatio(FLAG_ASPECT_RATIO)
                )
            }
            Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                with(sharedTransitionScope) {
                    Text(
                        text = country.commonName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.sharedBounds(
                            sharedTransitionScope.rememberSharedContentState(key = "name-${country.cca3}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = Dimens.PaddingSmall)
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(Dimens.IconSizeSmall)
                    )
                    Spacer(Modifier.width(Dimens.PaddingSmall))
                    Text(
                        text = country.region,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (country.capital.isNotBlank()) {
                    Text(
                        text = country.capital,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (!country.independent) {
                    Box(
                        modifier = Modifier
                            .padding(top = Dimens.PaddingMedium)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.list_not_independent),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                    selectedRegion = null,
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

private const val COUNTRIES_GRID_COLUMNS = 2
private const val SKELETON_ITEM_COUNT = 6
private const val FLAG_ASPECT_RATIO = 1.6f

private const val CARD_TITLE_SKELETON_WIDTH_FRACTION = 0.8f
private val CARD_TITLE_SKELETON_HEIGHT = 16.dp
private const val CARD_SUBTITLE_SKELETON_WIDTH_FRACTION = 0.5f
private val CARD_SUBTITLE_SKELETON_HEIGHT = 12.dp
