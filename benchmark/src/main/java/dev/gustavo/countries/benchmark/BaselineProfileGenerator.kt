package dev.gustavo.countries.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import dev.gustavo.countries.feature.list.ListTestTags

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

        // Wait for the list to load (using a test tag)
        device.wait(Until.hasObject(By.res(ListTestTags.countryCard("BRA"))), 15_000)

        // Scroll the list to pre-compile list item rendering
        val list = device.findObject(By.scrollable(true))
        if (list != null) {
            list.setGestureMargin(device.displayWidth / 10)
            list.scroll(Direction.DOWN, 0.8f)
            list.scroll(Direction.UP, 0.8f)
        }

        // Test search
        val searchField = device.findObject(By.res(ListTestTags.SEARCH_FIELD))
        if (searchField != null) {
            searchField.setText("United")
            device.wait(Until.hasObject(By.res(ListTestTags.countryCard("USA"))), 5000)
            
            val clearButton = device.findObject(By.res(ListTestTags.SEARCH_CLEAR_BUTTON))
            clearButton?.click()
        }

        // Click on a country to go to details and pre-compile detail screen
        val brazilCard = device.findObject(By.res(ListTestTags.countryCard("BRA")))
        if (brazilCard != null) {
            brazilCard.click()
            
            // Wait for detail screen to show up (look for capital text which is likely to be there)
            device.wait(Until.hasObject(By.text("Capital")), 5000)

            // Scroll in detail screen
            val detailList = device.findObject(By.scrollable(true))
            detailList?.scroll(Direction.DOWN, 0.5f)
            
            // Go back
            device.pressBack()
        }

        device.waitForIdle()
    }
}
