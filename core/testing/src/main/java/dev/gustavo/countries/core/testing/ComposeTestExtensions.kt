package dev.gustavo.countries.core.testing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import dev.gustavo.countries.core.ui.theme.CountriesTheme

/**
 * Sets the content for a Compose test with the app's theme and common transition scopes.
 * This helper avoids repeating the same boilerplate in every UI test.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
fun ComposeContentTestRule.setCountriesContent(
    content: @Composable (SharedTransitionScope, AnimatedContentScope) -> Unit
) {
    setContent {
        CountriesTheme {
            SharedTransitionLayout {
                val sharedScope = this
                @Suppress("UnusedContentLambdaTargetStateParameter")
                AnimatedContent(targetState = Unit, label = "test") {
                    val animatedScope = this
                    content(sharedScope, animatedScope)
                }
            }
        }
    }
}
