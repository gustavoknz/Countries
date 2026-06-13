
package dev.gustavo.countries.core.common.navigation

import kotlinx.serialization.Serializable

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
@Serializable
sealed interface Routes {
    @Serializable
    data object List : Routes

    @Serializable
    data class Detail(val countryCode: String) : Routes
}
