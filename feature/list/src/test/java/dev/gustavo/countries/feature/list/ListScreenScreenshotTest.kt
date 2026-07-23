package dev.gustavo.countries.feature.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.CompositionLocalProvider
import com.github.takahirom.roborazzi.captureRoboImage
import dev.gustavo.countries.core.testing.DEFAULT_ROBORAZZI_OPTIONS
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.util.LocalShimmerEnabled
import dev.gustavo.countries.feature.list.model.UiCountry
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
@OptIn(ExperimentalSharedTransitionApi::class)
class ListScreenScreenshotTest {

    @Test
    fun countryCard_screenshot() {
        val country = UiCountry(
            cca3 = "BRA",
            commonName = "Brazil",
            capital = "Brasília",
            flagUrl = "",
            region = "Americas",
            independent = true
        )

        captureRoboImage(
            filePath = "src/test/screenshots/country_card.png",
            roborazziOptions = DEFAULT_ROBORAZZI_OPTIONS
        ) {
            CountriesTheme {
                CompositionLocalProvider(LocalShimmerEnabled provides false) {
                    SharedTransitionLayout {
                        @Suppress("UnusedContentLambdaTargetStateParameter")
                        AnimatedContent(targetState = Unit, label = "test") {
                            CountryCard(
                                country = country,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun loadingSkeleton_screenshot() {
        captureRoboImage(
            filePath = "src/test/screenshots/loading_skeleton.png",
            roborazziOptions = DEFAULT_ROBORAZZI_OPTIONS
        ) {
            CountriesTheme {
                CompositionLocalProvider(LocalShimmerEnabled provides false) {
                    LoadingSkeletonGrid()
                }
            }
        }
    }
}
