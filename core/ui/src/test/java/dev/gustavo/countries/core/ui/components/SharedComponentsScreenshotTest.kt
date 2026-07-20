package dev.gustavo.countries.core.ui.components

import com.github.takahirom.roborazzi.captureRoboImage
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class SharedComponentsScreenshotTest {

    @Test
    fun errorState_screenshot() {
        captureRoboImage("src/test/screenshots/error_state.png") {
            CountriesTheme {
                ErrorState(
                    message = "Unable to load country data. Please check your internet connection and try again.",
                    retryLabel = "Retry",
                    onRetry = {}
                )
            }
        }
    }

    @Test
    fun emptyState_screenshot() {
        captureRoboImage("src/test/screenshots/empty_state.png") {
            CountriesTheme {
                EmptyState(message = "No countries match your search criteria.")
            }
        }
    }
}
