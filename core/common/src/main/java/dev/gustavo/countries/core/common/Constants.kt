package dev.gustavo.countries.core.common

object Constants {
    const val SEARCH_DEBOUNCE_DELAY_MS = 500L
    const val MAIN_LIST_QUERY_ID = "__MAIN_LIST__"

    private const val COMMA = "%2C"

    object ApiSchema {
        const val CCA3 = "codes.alpha_3"
        const val COMMON_NAME = "names.common"
        const val OFFICIAL_NAME = "names.official"
        const val CAPITALS = "capitals.name"
        const val FLAG_PNG = "flag.url_png"
        const val FLAG_SVG = "flag.url_svg"
        const val REGION = "region"
        const val SUBREGION = "subregion"
        const val LANGUAGES = "languages.name"
        const val POPULATION = "population"
        const val BORDERS = "borders"
        const val CURRENCY_NAME = "currencies.name"
        const val DEPENDENCY = "classification.dependency"
    }

    const val LIST_RESPONSE_FIELDS =
        ApiSchema.COMMON_NAME + COMMA +
        ApiSchema.CCA3 + COMMA +
        ApiSchema.CAPITALS + COMMA +
        ApiSchema.FLAG_PNG + COMMA +
        ApiSchema.REGION + COMMA +
        ApiSchema.DEPENDENCY

    const val DETAIL_RESPONSE_FIELDS =
        ApiSchema.COMMON_NAME + COMMA +
        ApiSchema.OFFICIAL_NAME + COMMA +
        ApiSchema.CCA3 + COMMA +
        ApiSchema.CAPITALS + COMMA +
        ApiSchema.FLAG_PNG + COMMA +
        ApiSchema.FLAG_SVG + COMMA +
        ApiSchema.REGION + COMMA +
        ApiSchema.SUBREGION + COMMA +
        ApiSchema.LANGUAGES + COMMA +
        ApiSchema.POPULATION + COMMA +
        ApiSchema.BORDERS + COMMA +
        ApiSchema.CURRENCY_NAME + COMMA +
        ApiSchema.DEPENDENCY
}
