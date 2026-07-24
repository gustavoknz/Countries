package dev.gustavo.countries.feature.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.list.R

@Composable
internal fun RegionFilterChips(
    selectedRegion: Region?,
    onRegionSelected: (Region?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = Dimens.PaddingExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingSmall),
    ) {
        item {
            FilterChip(
                selected = selectedRegion == null,
                onClick = { onRegionSelected(null) },
                label = { Text(stringResource(R.string.list_filter_all)) },
                leadingIcon =
                    if (selectedRegion == null) {
                        {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                modifier = Modifier.size(Dimens.IconSizeSmall),
                            )
                        }
                    } else {
                        null
                    },
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
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
                colors =
                    FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
            )
        }
    }
}
