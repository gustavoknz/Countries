package dev.gustavo.countries.feature.list.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.SkeletonItem
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.list.ListTestTags
import dev.gustavo.countries.feature.list.R
import dev.gustavo.countries.feature.list.model.UiCountry
import dev.gustavo.countries.core.ui.R as UiR

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
                    contentDescription = stringResource(UiR.string.common_flag_content_description, country.commonName),
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

@Composable
internal fun CountryCardSkeleton(
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

internal const val FLAG_ASPECT_RATIO = 1.6f
private const val CARD_TITLE_SKELETON_WIDTH_FRACTION = 0.8f
private val CARD_TITLE_SKELETON_HEIGHT = 16.dp
private const val CARD_SUBTITLE_SKELETON_WIDTH_FRACTION = 0.5f
private val CARD_SUBTITLE_SKELETON_HEIGHT = 12.dp
