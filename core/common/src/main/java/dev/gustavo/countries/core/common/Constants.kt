package dev.gustavo.countries.core.common

object Constants {
    const val SEARCH_DEBOUNCE_DELAY_MS = 500L
    const val LIST_RESPONSE_FIELDS = "names.common,codes.alpha_3,capitals.name,flag.url_png,region,classification.dependency"
    const val DETAIL_RESPONSE_FIELDS = "names.common,names.official,codes.alpha_3,capitals.name,flag.url_png,flag.url_svg,region,subregion,languages.name,population,borders,currencies.symbol,currencies.name,classification.dependency"
}
