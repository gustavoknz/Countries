package dev.gustavo.countries.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() = baselineProfileRule.collect(
        packageName = "dev.gustavo.countries",
        includeInStartupProfile = true,
    ) {
        // Start the app
        pressHome()
        startActivityAndWait()

        // Wait for the list to load (look for a country name)
        // We use a reasonably high timeout for the first load
        device.wait(Until.hasObject(By.text("Brazil")), 15_000)

        // Scroll the list to pre-compile list item rendering
        val list = device.findObject(By.scrollable(true))
        if (list != null) {
            list.setGestureMargin(device.displayWidth / 5)
            list.scroll(Direction.DOWN, 0.5f)
        }

        // Click on a country to go to details and pre-compile detail screen
        val countryCard = device.findObject(By.text("Brazil"))
        if (countryCard != null) {
            countryCard.click()
            
            // Wait for detail screen to show up
            device.wait(Until.hasObject(By.text("Capital")), 5000)

            // Scroll in detail screen
            val detailList = device.findObject(By.scrollable(true))
            detailList?.scroll(Direction.DOWN, 0.5f)
        }

        device.waitForIdle()
    }
}
