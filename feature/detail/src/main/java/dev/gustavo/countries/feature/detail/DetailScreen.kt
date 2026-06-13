package dev.gustavo.countries.feature.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.LoadingState
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat

@Composable
fun DetailRoute(
    countryCode: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(countryCode) {
        viewModel.onAction(DetailAction.LoadDetail(countryCode))
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is DetailEvent.NavigateBack -> onBack()
            }
        }
    }

    DetailScreen(
        viewState = viewState,
        onAction = viewModel::onAction
    )
}

@Composable
fun DetailScreen(
    viewState: DetailViewState,
    onAction: (DetailAction) -> Unit
) {
    val topBarTitle = (viewState as? DetailViewState.Loaded)?.country?.commonName.orEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
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
            is DetailViewState.Loading -> LoadingState(modifier = Modifier.padding(innerPadding))

            is DetailViewState.Loaded -> CountryDetailContent(
                country = viewState.country,
                modifier = Modifier.padding(innerPadding)
            )

            is DetailViewState.Error -> ErrorState(
                message = viewState.message,
                retryLabel = stringResource(R.string.detail_error_retry),
                onRetry = { viewState.countryCode?.let { onAction(DetailAction.LoadDetail(it)) } },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun CountryDetailContent(
    country: UiCountryDetail,
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
        FlagImage(
            url = country.flagUrl,
            contentDescription = stringResource(R.string.detail_flag_content_description, country.commonName),
            height = Dimens.FlagImageHeightLarge
        )

        Spacer(Modifier.height(Dimens.PaddingGiant))

        Text(
            text = country.commonName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

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
                if (country.independent) R.string.detail_independent_yes
                else R.string.detail_independent_no
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
            Text(
                text = country.borders.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(Dimens.PaddingMassive))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    CountriesTheme {
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
            onAction = {}
        )
    }
}
