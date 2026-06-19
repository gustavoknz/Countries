package dev.gustavo.countries.core.common

object Constants {
    const val SEARCH_DEBOUNCE_DELAY_MS = 500L
    const val MAIN_LIST_QUERY_ID = "__MAIN_LIST__"

    private const val COMMA = "%2C"

    const val LIST_RESPONSE_FIELDS =
        "names.common" + COMMA +
        "codes.alpha_3" + COMMA +
        "capitals.name" + COMMA +
        "flag.url_png" + COMMA +
        "region" + COMMA +
        "classification.dependency"

    const val DETAIL_RESPONSE_FIELDS =
        "names.common" + COMMA +
        "names.official" + COMMA +
        "codes.alpha_3" + COMMA +
        "capitals.name" + COMMA +
        "flag.url_png" + COMMA +
        "flag.url_svg" + COMMA +
        "region" + COMMA +
        "subregion" + COMMA +
        "languages.name" + COMMA +
        "population" + COMMA +
        "borders" + COMMA +
        "currencies.symbol" + COMMA +
        "currencies.name" + COMMA +
        "classification.dependency"
}
