package dev.gustavo.countries.core.ui.components

import androidx.compose.runtime.CompositionLocalProvider
import com.github.takahirom.roborazzi.captureRoboImage
import dev.gustavo.countries.core.testing.DEFAULT_ROBORAZZI_OPTIONS
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.util.LocalShimmerEnabled
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SharedComponentsScreenshotTest {

    @Test
    fun errorState_screenshot() {
        captureRoboImage(
            filePath = "src/test/screenshots/error_state.png",
            roborazziOptions = DEFAULT_ROBORAZZI_OPTIONS
        ) {
            CountriesTheme {
                CompositionLocalProvider(LocalShimmerEnabled provides false) {
                    ErrorState(
                        message = "Unable to load country data. Please check your internet connection and try again.",
                        retryLabel = "Retry",
                        onRetry = {}
                    )
                }
            }
        }
    }

    @Test
    fun emptyState_screenshot() {
        captureRoboImage(
            filePath = "src/test/screenshots/empty_state.png",
            roborazziOptions = DEFAULT_ROBORAZZI_OPTIONS
        ) {
            CountriesTheme {
                CompositionLocalProvider(LocalShimmerEnabled provides false) {
                    EmptyState(message = "No countries match your search criteria.")
                }
            }
        }
    }
}
