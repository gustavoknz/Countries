package dev.gustavo.countries.data.remote

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CapitalRemote
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.CurrencyRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.LanguageRemote
import dev.gustavo.countries.data.remote.model.MetaRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import dev.gustavo.countries.data.remote.model.toDetailDomain
import dev.gustavo.countries.data.remote.model.toDomain
import org.junit.Test

class CountryMappersTest {
    private val remote =
        CountryRemote(
            codes = CodesRemote(alpha3 = "BRA"),
            names = NameRemote(common = "Brazil", official = "Federative Republic of Brazil"),
            capitals = listOf(CapitalRemote(name = "Brasília")),
            flag = FlagRemote(png = "https://flagcdn.com/br.png", svg = "https://flagcdn.com/br.svg"),
            region = "Americas",
            subregion = "South America",
            languages = listOf(LanguageRemote(name = "Portuguese")),
            population = 215_000_000L,
            borders = listOf("ARG", "BOL", "COL"),
            currencies = listOf(CurrencyRemote(name = "Brazilian real")),
            classification = ClassificationRemote(dependency = false),
        )

    @Test
    fun `given valid remote when toCountry then maps all fields correctly`() {
        val country = remote.toDomain()

        assertThat(country.countryCode).isEqualTo("BRA")
        assertThat(country.commonName).isEqualTo("Brazil")
        assertThat(country.capital).isEqualTo("Brasília")
        assertThat(country.flagUrl).isEqualTo("https://flagcdn.com/br.png")
        assertThat(country.region).isEqualTo("Americas")
        assertThat(country.independent).isTrue()
    }

    @Test
    fun `given valid remote when toCountryDetail then maps all fields correctly`() {
        val detail = remote.toDetailDomain()

        assertThat(detail.countryCode).isEqualTo("BRA")
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
        val emptyRemote =
            CountryRemote(
                codes = null,
                names = null,
                capitals = null,
                flag = null,
                region = null,
                subregion = null,
                languages = null,
                population = null,
                borders = null,
                currencies = null,
                classification = null,
            )

        val country = emptyRemote.toDomain()

        assertThat(country.countryCode).isEmpty()
        assertThat(country.commonName).isEmpty()
        assertThat(country.capital).isEmpty()
        assertThat(country.flagUrl).isEmpty()
        assertThat(country.region).isEmpty()
        assertThat(country.independent).isTrue()
    }

    @Test
    fun `given remote with multiple capitals when toCountry then uses first capital`() {
        val multiCapital = remote.copy(capitals = listOf(CapitalRemote("Brasília"), CapitalRemote("São Paulo")))

        val country = multiCapital.toDomain()

        assertThat(country.capital).isEqualTo("Brasília")
    }

    @Test
    fun `given remote with dependency true when toCountry then independent is false`() {
        val dependent = remote.copy(classification = ClassificationRemote(dependency = true))

        assertThat(dependent.toDomain().independent).isFalse()
        assertThat(dependent.toDetailDomain().independent).isFalse()
    }

    @Test
    fun `given remote with null dependency when toCountry then independent is true`() {
        val noClassification = remote.copy(classification = null)
        val nullDependency = remote.copy(classification = ClassificationRemote(dependency = null))

        assertThat(noClassification.toDomain().independent).isTrue()
        assertThat(nullDependency.toDomain().independent).isTrue()
    }

    @Test
    fun `given collections with null names when toCountryDetail then filters them out`() {
        val remoteWithNulls =
            remote.copy(
                languages = listOf(LanguageRemote(name = "English"), LanguageRemote(name = null)),
                currencies = listOf(CurrencyRemote(name = null), CurrencyRemote(name = "Euro")),
            )

        val detail = remoteWithNulls.toDetailDomain()

        assertThat(detail.languages).containsExactly("English")
        assertThat(detail.currencies).containsExactly("Euro")
    }

    @Test
    fun `given remote with no official name when toCountryDetail then uses empty string`() {
        val noOfficialName = remote.copy(names = NameRemote(common = "Brazil", official = null))

        assertThat(noOfficialName.toDetailDomain().officialName).isEmpty()
    }

    @Test
    fun `test BaseResponse and DataWrapper structure`() {
        val meta =
            MetaRemote(
                total = 100,
                count = 10,
                limit = 10,
                offset = 0,
                more = true,
            )
        val wrapper =
            DataWrapper(
                objects = listOf(remote),
                meta = meta,
            )
        val response = BaseResponse(data = wrapper)

        assertThat(response.data).isEqualTo(wrapper)
        assertThat(response.data?.objects).containsExactly(remote)
        assertThat(response.data?.meta).isEqualTo(meta)
        assertThat(meta.total).isEqualTo(100)
        assertThat(meta.count).isEqualTo(10)
        assertThat(meta.limit).isEqualTo(10)
        assertThat(meta.offset).isEqualTo(0)
        assertThat(meta.more).isTrue()
    }

    @Test
    fun `test FlagRemote secondary properties`() {
        assertThat(remote.flag?.svg).isEqualTo("https://flagcdn.com/br.svg")
    }
}
