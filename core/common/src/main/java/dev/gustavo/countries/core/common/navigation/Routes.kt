
package dev.gustavo.countries.core.common.navigation

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
sealed interface Routes {
    @Serializable
    data object List : Routes

    @Serializable
    data class Detail(val countryCode: String, val flagUrl: String? = null) : Routes
}
