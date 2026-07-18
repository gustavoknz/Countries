package dev.gustavo.countries.feature.detail.model

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.testing.TestData
import dev.gustavo.countries.core.ui.util.UiText
import org.junit.After
import org.junit.Test
import java.util.Locale

class UiCountryDetailTest {

    @After
    fun tearDown() {
        val originalLocale = Locale.getDefault()
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `given population in US locale when toUiModel then formats with commas`() {
        Locale.setDefault(Locale.US)
        val domainModel = TestData.createCountryDetail(population = 1_234_567L)

        val uiModel = domainModel.toUiModel()

        val populationText = uiModel.population as UiText.DynamicString
        assertThat(populationText.value).isEqualTo("1,234,567")
    }

    @Test
    fun `given population in Brazil locale when toUiModel then formats with dots`() {
        Locale.setDefault(Locale.forLanguageTag("pt-BR"))
        val domainModel = TestData.createCountryDetail(population = 1_234_567L)

        val uiModel = domainModel.toUiModel()

        val populationText = uiModel.population as UiText.DynamicString
        // Note: NBSP might be used depending on Java version, but usually it's dot for pt-BR
        assertThat(populationText.value).isEqualTo("1.234.567")
    }
}
