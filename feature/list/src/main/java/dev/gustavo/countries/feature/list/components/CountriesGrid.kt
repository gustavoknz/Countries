package dev.gustavo.countries.feature.list.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.list.model.UiCountry

@Composable
internal fun CountriesGrid(
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

private const val COUNTRIES_GRID_COLUMNS = 2
private const val SKELETON_ITEM_COUNT = 6
