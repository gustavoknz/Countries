package dev.gustavo.countries.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.SkeletonItem
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

const val TOP_BAR_TITLE = "detail_top_app_bar_title"
const val COMMON_NAME = "detail_common_name"
const val DETAIL_SKELETON = "detail_skeleton"
const val DETAIL_CONTENT = "detail_content"
const val BACK_BUTTON = "detail_back_button"

@Composable
fun DetailRoute(
    countryCode: String,
    flagUrl: String?,
    onBack: () -> Unit,
    onCountryClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(countryCode, flagUrl) {
        viewModel.onAction(DetailAction.LoadDetail(countryCode, flagUrl))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DetailEvent.NavigateBack -> onBack()
                is DetailEvent.NavigateToDetail -> onCountryClick(event.cca3)
            }
        }
    }

    DetailScreen(
        viewState = viewState,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onAction = viewModel::onAction
    )
}

@Composable
fun DetailScreen(
    viewState: DetailViewState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onAction: (DetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val topBarTitle = (viewState as? DetailViewState.Loaded)?.country?.commonName.orEmpty()
                    Text(
                        text = topBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag(TOP_BAR_TITLE)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(DetailAction.BackClicked) },
                        modifier = Modifier.testTag(BACK_BUTTON)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back_button_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            AnimatedContent(
                targetState = viewState,
                label = "detail_state_transition"
            ) { state ->
                when (state) {
                    is DetailViewState.Loading -> DetailSkeleton(
                        cca3 = state.cca3,
                        flagUrl = state.flagUrl,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(DETAIL_SKELETON)
                    )

                    is DetailViewState.Loaded -> CountryDetailContent(
                        country = state.country,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(DETAIL_CONTENT)
                    )

                    is DetailViewState.Error -> ErrorState(
                        message = state.message.asString(),
                        retryLabel = stringResource(R.string.detail_error_retry),
                        onRetry = { state.countryCode?.let { onAction(DetailAction.LoadDetail(it)) } },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryDetailContent(
    country: UiCountryDetail,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onAction: (DetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
    ) {
        Card(
            shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                with(sharedTransitionScope) {
                    FlagImage(
                        url = country.flagUrl,
                        contentDescription = country.flagContentDescription.asString(),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = "flag-${country.cca3}"),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .height(Dimens.FlagImageHeightLarge)
                            .fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(Dimens.PaddingGiant))

                with(sharedTransitionScope) {
                    Text(
                        text = country.commonName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .sharedBounds(
                                sharedTransitionScope.rememberSharedContentState(key = "name-${country.cca3}"),
                                animatedVisibilityScope = animatedContentScope
                            )
                            .testTag(COMMON_NAME)
                    )
                }

                if (country.officialName != country.commonName) {
                    Text(
                        text = country.officialName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        SectionCard(title = stringResource(R.string.detail_section_geography), icon = Icons.Default.LocationOn) {
            DetailRow(
                icon = Icons.Default.LocationCity,
                label = UiText.StringResource(R.string.detail_label_capital),
                value = country.capital
            )
            DetailRow(
                icon = Icons.Default.Public,
                label = UiText.StringResource(R.string.detail_label_region),
                value = country.region
            )
            DetailRow(
                icon = Icons.Default.LocationOn,
                label = UiText.StringResource(R.string.detail_label_subregion),
                value = country.subregion
            )
            
            if (country.borders.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.detail_label_bordering_countries),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = Dimens.PaddingMedium)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                    modifier = Modifier.padding(top = Dimens.PaddingSmall)
                ) {
                    country.borders.forEach { cca3 ->
                        AssistChip(
                            onClick = { onAction(DetailAction.BorderClicked(cca3)) },
                            label = { Text(cca3) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            }
        }

        SectionCard(title = stringResource(R.string.detail_section_demographics), icon = Icons.Default.People) {
            DetailRow(
                icon = Icons.Default.People,
                label = UiText.StringResource(R.string.detail_label_population),
                value = country.population
            )
            DetailRow(
                icon = Icons.Default.Language,
                label = UiText.StringResource(R.string.detail_label_languages),
                value = country.languages
            )
            DetailRow(
                icon = Icons.Default.VerifiedUser,
                label = UiText.StringResource(R.string.detail_label_independent),
                value = country.independent
            )
        }

        SectionCard(title = stringResource(R.string.detail_section_economy), icon = Icons.Default.MonetizationOn) {
            DetailRow(
                icon = Icons.Default.AccountBalance,
                label = UiText.StringResource(R.string.detail_label_currencies),
                value = country.currencies
            )
        }

        Spacer(Modifier.height(Dimens.PaddingMassive))
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingLarge)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(Dimens.PaddingMedium))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Column(content = content)
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: UiText,
    value: UiText
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = label.asString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value.asString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailSkeleton(
    cca3: String?,
    flagUrl: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(Dimens.PaddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
    ) {
        Card(
            shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(Dimens.PaddingLarge), horizontalAlignment = Alignment.CenterHorizontally) {
                if (cca3 != null && flagUrl != null) {
                    with(sharedTransitionScope) {
                        FlagImage(
                            url = flagUrl,
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "flag-$cca3"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                                .height(Dimens.FlagImageHeightLarge)
                                .fillMaxWidth()
                        )
                    }
                } else {
                    SkeletonItem(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(Dimens.FlagImageHeightLarge)
                    )
                }

                Spacer(Modifier.height(Dimens.PaddingGiant))

                SkeletonItem(modifier = Modifier.width(200.dp).height(32.dp))
                Spacer(Modifier.height(8.dp))
                SkeletonItem(modifier = Modifier.width(250.dp).height(20.dp))
            }
        }

        repeat(2) {
            Card(
                shape = RoundedCornerShape(Dimens.CornerRadiusMedium),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(Dimens.PaddingLarge)) {
                    SkeletonItem(modifier = Modifier.width(100.dp).height(24.dp))
                    Spacer(Modifier.height(16.dp))
                    repeat(3) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            SkeletonItem(modifier = Modifier.width(80.dp).height(16.dp))
                            SkeletonItem(modifier = Modifier.width(120.dp).height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    CountriesTheme {
        SharedTransitionLayout {
            @Suppress("UnusedContentLambdaTargetStateParameter")
            AnimatedContent(targetState = Unit, label = "preview") {
                DetailScreen(
                    viewState = DetailViewState.Loaded(
                        country = UiCountryDetail(
                            cca3 = "BRA",
                            commonName = "Brazil",
                            officialName = "Federative Republic of Brazil",
                            flagUrl = "",
                            flagContentDescription = UiText.DynamicString("Brazil flag"),
                            capital = UiText.DynamicString("Brasília"),
                            independent = UiText.DynamicString("Yes"),
                            region = UiText.DynamicString("Americas"),
                            subregion = UiText.DynamicString("South America"),
                            population = UiText.DynamicString("215,000,000"),
                            languages = UiText.DynamicString("Portuguese"),
                            currencies = UiText.DynamicString("Brazilian real"),
                            bordersCount = UiText.DynamicString("11"),
                            borders = persistentListOf("ARG", "BOL", "COL", "GUF", "GUY", "PAR", "PER", "PRY", "SUR")
                        )
                    ),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this,
                    onAction = {}
                )
            }
        }
    }
}
