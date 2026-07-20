package dev.gustavo.countries.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import com.github.takahirom.roborazzi.captureRoboImage
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.feature.detail.model.UiCountryDetail
import kotlinx.collections.immutable.persistentListOf
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@OptIn(ExperimentalSharedTransitionApi::class)
class DetailScreenScreenshotTest {

    @Test
    fun detailLoaded_screenshot() {
        val country = UiCountryDetail(
            cca3 = "BRA",
            commonName = "Brazil",
            officialName = "Federative Republic of Brazil",
            flagUrl = "",
            flagContentDescription = UiText.DynamicString("Brazil flag"),
            capital = UiText.DynamicString("Brasília"),
            independent = UiText.DynamicString("Yes"),
            region = UiText.DynamicString("Americas"),
            subregion = UiText.DynamicString("South America"),
            population = UiText.DynamicString("215,000,000"),
            languages = UiText.DynamicString("Portuguese"),
            currencies = UiText.DynamicString("Brazilian real"),
            bordersCount = UiText.DynamicString("1"),
            borders = persistentListOf("ARG")
        )

        captureRoboImage("src/test/screenshots/detail_loaded.png") {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailScreen(
                            viewState = DetailViewState.Loaded(country),
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                            onAction = {}
                        )
                    }
                }
            }
        }
    }

    @Test
    fun detailSkeleton_screenshot() {
        captureRoboImage("src/test/screenshots/detail_skeleton.png") {
            CountriesTheme {
                SharedTransitionLayout {
                    @Suppress("UnusedContentLambdaTargetStateParameter")
                    AnimatedContent(targetState = Unit, label = "test") {
                        DetailSkeleton(
                            cca3 = "BRA",
                            flagUrl = "",
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this
                        )
                    }
                }
            }
        }
    }
}
