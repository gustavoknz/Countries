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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gustavo.countries.core.ui.components.ErrorState
import dev.gustavo.countries.core.ui.components.FlagImage
import dev.gustavo.countries.core.ui.components.LoadingState
import dev.gustavo.countries.domain.model.CountryDetail
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    countryCode: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
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

    val topBarTitle = (viewState as? DetailViewState.Loaded)?.country?.commonName.orEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onAction(DetailAction.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.detail_back_button_description),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
    ) { innerPadding ->
        when (val state = viewState) {
            is DetailViewState.Loading -> LoadingState(modifier = Modifier.padding(innerPadding))

            is DetailViewState.Loaded -> CountryDetailContent(
                country = state.country,
                modifier = Modifier.padding(innerPadding),
            )

            is DetailViewState.Error -> ErrorState(
                message = state.message,
                retryLabel = stringResource(R.string.detail_error_retry),
                onRetry = { viewModel.onAction(DetailAction.LoadDetail(countryCode)) },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun CountryDetailContent(
    country: CountryDetail,
    modifier: Modifier = Modifier,
) {
    val emptyValue = stringResource(R.string.detail_empty_value)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        FlagImage(
            url = country.flagUrl,
            contentDescription = stringResource(R.string.detail_flag_content_description, country.commonName),
            height = 200.dp,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = country.commonName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        if (country.officialName != country.commonName) {
            Text(
                text = country.officialName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(20.dp))

        DetailRow(
            label = stringResource(R.string.detail_label_capital),
            value = country.capital.ifBlank { emptyValue },
        )
        DetailRow(
            label = stringResource(R.string.detail_label_region),
            value = country.region.ifBlank { emptyValue },
        )
        DetailRow(
            label = stringResource(R.string.detail_label_subregion),
            value = country.subregion.ifBlank { emptyValue },
        )
        DetailRow(
            label = stringResource(R.string.detail_label_population),
            value = NumberFormat.getNumberInstance(Locale.getDefault()).format(country.population),
        )
        DetailRow(
            label = stringResource(R.string.detail_label_languages),
            value = country.languages.joinToString(", ").ifBlank { emptyValue },
        )
        DetailRow(
            label = stringResource(R.string.detail_label_currencies),
            value = country.currencies.joinToString(", ").ifBlank { emptyValue },
        )
        DetailRow(
            label = stringResource(R.string.detail_label_bordering_countries),
            value = if (country.borders.isEmpty()) {
                stringResource(R.string.detail_no_borders)
            } else {
                country.borders.size.toString()
            },
        )

        if (country.borders.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = country.borders.joinToString(" · "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.45f),
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.55f),
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
}
