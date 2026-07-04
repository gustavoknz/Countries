package dev.gustavo.countries.feature.detail.model

import androidx.compose.runtime.Immutable
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.feature.detail.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.text.NumberFormat

@Immutable
data class UiCountryDetail(
    val cca3: String,
    val commonName: String,
    val officialName: String,
    val flagUrl: String,
    val flagContentDescription: UiText,
    val capital: UiText,
    val independent: UiText,
    val region: UiText,
    val subregion: UiText,
    val population: UiText,
    val languages: UiText,
    val currencies: UiText,
    val bordersCount: UiText,
    val borders: ImmutableList<String>
)

fun CountryDetail.toUiModel(): UiCountryDetail {
    return UiCountryDetail(
        cca3 = cca3,
        commonName = commonName,
        officialName = officialName,
        flagUrl = flagUrl,
        flagContentDescription = UiText.StringResource(R.string.detail_flag_content_description, commonName),
        capital = if (capital.isBlank()) UiText.StringResource(R.string.detail_empty_value) else UiText.DynamicString(capital),
        independent = if (independent) {
            UiText.StringResource(R.string.detail_independent_yes)
        } else {
            UiText.StringResource(R.string.detail_independent_no)
        },
        region = if (region.isBlank()) UiText.StringResource(R.string.detail_empty_value) else UiText.DynamicString(region),
        subregion = if (subregion.isBlank()) {
            UiText.StringResource(R.string.detail_empty_value)
        } else {
            UiText.DynamicString(subregion)
        },
        population = UiText.DynamicString(NumberFormat.getNumberInstance().format(population)),
        languages = if (languages.isEmpty()) {
            UiText.StringResource(R.string.detail_empty_value)
        } else {
            UiText.DynamicString(languages.joinToString(", "))
        },
        currencies = if (currencies.isEmpty()) {
            UiText.StringResource(R.string.detail_empty_value)
        } else {
            UiText.DynamicString(currencies.joinToString(", "))
        },
        bordersCount = if (borders.isEmpty()) {
            UiText.StringResource(R.string.detail_no_borders)
        } else {
            UiText.DynamicString(borders.size.toString())
        },
        borders = borders.toImmutableList()
    )
}
