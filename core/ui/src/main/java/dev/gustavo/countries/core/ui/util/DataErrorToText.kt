package dev.gustavo.countries.core.ui.util

import dev.gustavo.countries.core.common.DataError
import dev.gustavo.countries.core.ui.R

fun DataError.toUiText(): UiText {
    val resId = when (this) {
        DataError.NoConnection -> R.string.error_no_internet
        DataError.Timeout -> R.string.error_timeout
        DataError.ServerError -> R.string.error_server
        DataError.Forbidden -> R.string.error_forbidden
        DataError.Serialization -> R.string.error_serialization
        DataError.Unknown -> R.string.error_unknown
    }
    return UiText.StringResource(resId)
}
