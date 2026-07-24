package dev.gustavo.countries.feature.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.gustavo.countries.core.common.DataError
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.ui.components.EmptyState
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.util.toUiText
import dev.gustavo.countries.feature.list.R
import dev.gustavo.countries.core.ui.R as UiR

@Composable
internal fun ListContent(
    isLoading: Boolean,
    showEmptyState: Boolean,
    error: DataError?,
    searchQuery: String,
    selectedRegion: Region?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    when {
        isLoading && !showEmptyState -> {
            LoadingSkeletonGrid(modifier)
        }

        error != null -> {
            ErrorState(
                message = error.toUiText().asString(),
                retryLabel = stringResource(UiR.string.common_retry),
                onRetry = onRetry,
                modifier = modifier,
            )
        }

        showEmptyState -> {
            val emptyMessage =
                when {
                    searchQuery.isNotBlank() && selectedRegion != null -> {
                        stringResource(
                            R.string.list_empty_search_with_region_result,
                            searchQuery,
                            selectedRegion.apiValue,
                        )
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
