package dev.gustavo.countries.feature.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gustavo.countries.core.ui.components.EmptyState
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.LoadingState
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.list.model.UiCountry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListScreen(
    onCountryClick: (String) -> Unit,
    viewModel: ListViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onAction(ListAction.LoadCountries)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is ListEvent.NavigateToDetail -> onCountryClick(event.cca3)
            }
        }
    }

    ListScreenContent(
        viewState = viewState,
        searchQuery = searchQuery,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ListScreenContent(
    viewState: ListViewState,
    searchQuery: String,
    onAction: (ListAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
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
                    singleLine = true,
                    shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onAction(ListAction.SearchTriggered)
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
                                color = if (viewState.isOffline) MaterialTheme.colorScheme.error else Color.Transparent
                            ),
                            shape = RoundedCornerShape(Dimens.CornerRadiusMedium)
                        )
                )
            }
        }
    ) { innerPadding ->
        val isRefreshing = when (viewState) {
            is ListViewState.Loaded -> viewState.isRefreshing
            is ListViewState.Error -> viewState.isRefreshing
            else -> false
        }
        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onAction(ListAction.Refresh) },
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (viewState) {
                is ListViewState.Loading -> LoadingState()

                is ListViewState.Loaded -> {
                    if (viewState.countries.isEmpty()) {
                        EmptyState(
                            message = stringResource(R.string.list_empty_result)
                        )
                    } else {
                        CountriesGrid(
                            countries = viewState.countries,
                            onCountryClick = { onAction(ListAction.CountryClicked(it)) }
                        )
                    }
                }

                is ListViewState.Error -> ErrorState(
                    message = viewState.message,
                    retryLabel = stringResource(R.string.list_error_retry),
                    onRetry = { onAction(ListAction.LoadCountries) }
                )
            }
        }
    }
}

@Composable
private fun CountriesGrid(
    countries: ImmutableList<UiCountry>,
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
        item(span = { GridItemSpan(maxLineSpan) }) {
            StatisticsHeader(countries = countries)
        }

        items(items = countries, key = { it.cca3 }) { country ->
            CountryCard(country = country, onClick = { onCountryClick(country.cca3) })
        }
    }
}

@Composable
private fun StatisticsHeader(
    countries: ImmutableList<UiCountry>,
    modifier: Modifier = Modifier
) {
    val total = countries.size
    val independent = countries.count { it.independent }
    val notIndependent = total - independent

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimens.ElevationSmall
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = Dimens.PaddingMedium, horizontal = Dimens.PaddingSmall),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                label = stringResource(R.string.list_stat_total, total),
                color = MaterialTheme.colorScheme.primary
            )
            StatItem(
                label = stringResource(R.string.list_stat_independent, independent),
                color = MaterialTheme.colorScheme.tertiary
            )
            StatItem(
                label = stringResource(R.string.list_stat_not_independent, notIndependent),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun CountryCard(
    country: UiCountry,
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
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationMedium),
        border = border
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
            FlagImage(
                url = country.flagUrl,
                contentDescription = stringResource(R.string.list_flag_content_description, country.commonName),
                height = Dimens.FlagImageHeightMedium
            )
            Column(modifier = Modifier.padding(top = Dimens.PaddingMedium)) {
                Text(
                    text = country.commonName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
    CountriesTheme {
        ListScreenContent(
            viewState = ListViewState.Loaded(
                countries = persistentListOf(
                    UiCountry("BRA", "Brazil", "Brasília", "", "Americas", true),
                    UiCountry("GRL", "Greenland", "Nuuk", "", "Americas", false)
                )
            ),
            searchQuery = "",
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CountryCardPreview() {
    CountriesTheme {
        CountryCard(
            country = UiCountry("BRA", "Brazil", "Brasília", "", "Americas", false),
            onClick = {}
        )
    }
}
