package dev.gustavo.countries.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
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
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat

const val TOP_BAR_TITLE = "detail_top_app_bar_title"
const val COMMON_NAME = "detail_common_name"

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
                    IconButton(onClick = { onAction(DetailAction.BackClicked) }) {
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
        when (viewState) {
            is DetailViewState.Loading -> DetailSkeleton(
                cca3 = viewState.cca3,
                flagUrl = viewState.flagUrl,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                modifier = Modifier.padding(innerPadding)
            )

            is DetailViewState.Loaded -> CountryDetailContent(
                country = viewState.country,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onAction = onAction,
                modifier = Modifier.padding(innerPadding)
            )

            is DetailViewState.Error -> ErrorState(
                message = viewState.message.asString(),
                retryLabel = stringResource(R.string.detail_error_retry),
                onRetry = { viewState.countryCode?.let { onAction(DetailAction.LoadDetail(it)) } },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CountryDetailContent(
    country: UiCountryDetail,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onAction: (DetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val emptyValue = stringResource(R.string.detail_empty_value)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.PaddingHuge, vertical = Dimens.PaddingExtraLarge),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        with(sharedTransitionScope) {
            FlagImage(
                url = country.flagUrl,
                contentDescription = stringResource(R.string.detail_flag_content_description, country.commonName),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .sharedElement(
                        sharedTransitionScope.rememberSharedContentState(key = "flag-${country.cca3}"),
                        animatedVisibilityScope = animatedContentScope
                    )
                    .height(Dimens.FlagImageHeightLarge)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(Dimens.PaddingGiant))

        with(sharedTransitionScope) {
            Text(
                text = country.commonName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(Dimens.PaddingGiant))

        HorizontalDivider()

        Spacer(Modifier.height(Dimens.PaddingHuge))

        DetailRow(
            label = stringResource(R.string.detail_label_capital),
            value = country.capital.ifBlank { emptyValue }
        )
        DetailRow(
            label = stringResource(R.string.detail_label_independent),
            value = stringResource(
                if (country.independent) R.string.detail_independent_yes else R.string.detail_independent_no
            )
        )
        DetailRow(
            label = stringResource(R.string.detail_label_region),
            value = country.region.ifBlank { emptyValue }
        )
        DetailRow(
            label = stringResource(R.string.detail_label_subregion),
            value = country.subregion.ifBlank { emptyValue }
        )
        DetailRow(
            label = stringResource(R.string.detail_label_population),
            value = NumberFormat.getNumberInstance().format(country.population)
        )
        DetailRow(
            label = stringResource(R.string.detail_label_languages),
            value = country.languages.joinToString(", ").ifBlank { emptyValue }
        )
        DetailRow(
            label = stringResource(R.string.detail_label_currencies),
            value = country.currencies.joinToString(", ").ifBlank { emptyValue }
        )
        DetailRow(
            label = stringResource(R.string.detail_label_bordering_countries),
            value = if (country.borders.isEmpty()) {
                stringResource(R.string.detail_no_borders)
            } else {
                country.borders.size.toString()
            }
        )

        if (country.borders.isNotEmpty()) {
            Spacer(Modifier.height(Dimens.PaddingSmall))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                country.borders.forEach { cca3 ->
                    AssistChip(
                        onClick = { onAction(DetailAction.BorderClicked(cca3)) },
                        label = { Text(cca3) }
                    )
                }
            }
        }

        Spacer(Modifier.height(Dimens.PaddingMassive))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingLarge),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.55f)
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingHuge, vertical = Dimens.PaddingExtraLarge),
    ) {
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
                        .align(Alignment.CenterHorizontally)
                )
            }
        } else {
            SkeletonItem(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(Dimens.FlagImageHeightLarge)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(Dimens.PaddingGiant))

        SkeletonItem(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(32.dp)
        )

        Spacer(Modifier.height(Dimens.PaddingSmall))

        SkeletonItem(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(20.dp)
        )

        Spacer(Modifier.height(Dimens.PaddingGiant))

        HorizontalDivider()

        Spacer(Modifier.height(Dimens.PaddingHuge))

        repeat(6) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.PaddingLarge),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SkeletonItem(
                    modifier = Modifier
                        .width(100.dp)
                        .height(16.dp)
                )
                SkeletonItem(
                    modifier = Modifier
                        .width(150.dp)
                        .height(16.dp)
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
                            capital = "Brasília",
                            flagUrl = "",
                            region = "Americas",
                            subregion = "South America",
                            languages = listOf("Portuguese"),
                            population = 215000000,
                            borders = listOf("ARG", "BOL", "COL", "GUF", "GUY", "PAR", "PER", "PRY", "SUR", "URU", "VEN"),
                            currencies = listOf("Brazilian real"),
                            independent = true
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

@Preview(showBackground = true)
@Composable
private fun LoadingStatePreview() {
    CountriesTheme {
        SharedTransitionLayout {
            @Suppress("UnusedContentLambdaTargetStateParameter")
            AnimatedContent(targetState = Unit, label = "preview") {
                DetailSkeleton(
                    cca3 = null,
                    flagUrl = null,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this
                )
            }
        }
    }
}
