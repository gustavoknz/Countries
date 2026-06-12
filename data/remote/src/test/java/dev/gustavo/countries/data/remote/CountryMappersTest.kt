package dev.gustavo.countries.data.remote

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.data.remote.model.CapitalRemote
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.CurrencyRemote
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.LanguageRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import dev.gustavo.countries.data.remote.model.toDetailDomain
import dev.gustavo.countries.data.remote.model.toDomain
import org.junit.Test

class CountryMappersTest {

    private val remote = CountryRemote(
        codes = CodesRemote(alpha3 = "BRA"),
        names = NameRemote(common = "Brazil", official = "Federative Republic of Brazil"),
        capitals = listOf(CapitalRemote(name = "Brasília")),
        flag = FlagRemote(png = "https://flagcdn.com/br.png", svg = null),
        region = "Americas",
        subregion = "South America",
        languages = listOf(LanguageRemote(name = "Portuguese")),
        population = 215_000_000L,
        borders = listOf("ARG", "BOL", "COL"),
        currencies = listOf(CurrencyRemote(name = "Brazilian real", symbol = "R$")),
        classification = ClassificationRemote(dependency = false)
    )

    @Test
    fun `given valid remote when toCountry then maps all fields correctly`() {
        val country = remote.toDomain()

        assertThat(country.cca3).isEqualTo("BRA")
        assertThat(country.commonName).isEqualTo("Brazil")
        assertThat(country.capital).isEqualTo("Brasília")
        assertThat(country.flagUrl).isEqualTo("https://flagcdn.com/br.png")
        assertThat(country.region).isEqualTo("Americas")
    }

    @Test
    fun `given valid remote when toCountryDetail then maps all fields correctly`() {
        val detail = remote.toDetailDomain()

        assertThat(detail.cca3).isEqualTo("BRA")
        assertThat(detail.commonName).isEqualTo("Brazil")
        assertThat(detail.officialName).isEqualTo("Federative Republic of Brazil")
        assertThat(detail.capital).isEqualTo("Brasília")
        assertThat(detail.region).isEqualTo("Americas")
        assertThat(detail.subregion).isEqualTo("South America")
        assertThat(detail.languages).containsExactly("Portuguese")
        assertThat(detail.population).isEqualTo(215_000_000L)
        assertThat(detail.borders).containsExactly("ARG", "BOL", "COL")
        assertThat(detail.currencies).containsExactly("Brazilian real")
    }

    @Test
    fun `given remote with null fields when toCountry then uses empty defaults`() {
        val emptyRemote = CountryRemote(
            codes = null, names = null, capitals = null,
            flag = null, region = null, subregion = null,
            languages = null, population = null, borders = null, currencies = null,
            classification = null
        )

        val country = emptyRemote.toDomain()

        assertThat(country.cca3).isEmpty()
        assertThat(country.commonName).isEmpty()
        assertThat(country.capital).isEmpty()
    }

    @Test
    fun `given remote with multiple capitals when toCountry then uses first capital`() {
        val multiCapital = remote.copy(capitals = listOf(CapitalRemote("Brasília"), CapitalRemote("São Paulo")))

        val country = multiCapital.toDomain()

        assertThat(country.capital).isEqualTo("Brasília")
    }
}
