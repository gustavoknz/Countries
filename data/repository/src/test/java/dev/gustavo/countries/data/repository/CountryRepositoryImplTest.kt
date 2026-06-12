package dev.gustavo.countries.data.repository

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CapitalRemote
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CountryRepositoryImplTest {

    private val api: CountryApiService = mockk()
    private val countryDao: CountryDao = mockk(relaxed = true)
    private val countryDetailDao: CountryDetailDao = mockk(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: CountryRepositoryImpl

    private val fakeDispatcherProvider = object : dev.gustavo.countries.core.common.DispatcherProvider {
        override fun io() = dispatcher
        override fun main() = dispatcher
        override fun default() = dispatcher
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = CountryRepositoryImpl(api, countryDao, countryDetailDao, fakeDispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── getCountries ──────────────────────────────────────────────────────────

    @Test
    fun `given empty cache when getCountries then fetches from api and caches`() = runTest {
        val remoteCountry = CountryRemote(
            codes = CodesRemote("BRA"),
            names = NameRemote("Brazil", "Federal Republic of Brazil"),
            capitals = listOf(CapitalRemote("Brasília")),
            flag = FlagRemote("url", null),
            region = "Americas",
            subregion = null,
            languages = null,
            population = null,
            borders = null,
            currencies = null,
            classification = ClassificationRemote(dependency = false)
        )
        val entity = CountryEntity(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "url", region = "Americas", independent = true)

        coEvery { countryDao.getAllCountries() } returnsMany listOf(emptyList(), listOf(entity))
        coEvery { api.getAllCountries() } returns BaseResponse<CountryRemote>(DataWrapper(listOf(remoteCountry)))

        val result = repository.getCountries()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(1)
        assertThat(result.getOrNull()?.first()?.cca3).isEqualTo("BRA")
        coVerify(exactly = 1) { api.getAllCountries() }
        coVerify(exactly = 1) { countryDao.insertAll(any()) }
    }

    @Test
    fun `given populated cache when getCountries then returns cached data without api call`() = runTest {
        coEvery { countryDao.getAllCountries() } returns listOf(
            CountryEntity(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas", independent = true)
        )

        val result = repository.getCountries()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.first()?.cca3).isEqualTo("BRA")
        coVerify(exactly = 0) { api.getAllCountries() }
    }

    @Test
    fun `given api failure when getCountries then returns failure`() = runTest {
        coEvery { countryDao.getAllCountries() } returns emptyList()
        coEvery { api.getAllCountries() } throws RuntimeException("Network error")

        val result = repository.getCountries()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `given populated cache and forceRefresh when getCountries then fetches from api`() = runTest {
        val remoteCountry = CountryRemote(
            codes = CodesRemote("BRA"),
            names = NameRemote("Brazil", "Federal Republic of Brazil"),
            capitals = listOf(CapitalRemote("Brasília")),
            flag = FlagRemote("url", null),
            region = "Americas",
            subregion = null,
            languages = null,
            population = null,
            borders = null,
            currencies = null,
            classification = ClassificationRemote(dependency = false)
        )
        val entity = CountryEntity(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "url", region = "Americas", independent = true)

        coEvery { api.getAllCountries() } returns BaseResponse<CountryRemote>(DataWrapper(listOf(remoteCountry)))
        coEvery { countryDao.getAllCountries() } returns listOf(entity)

        val result = repository.getCountries(forceRefresh = true)

        assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) { api.getAllCountries() }
        coVerify(exactly = 1) { countryDao.refreshCountries(any()) }
    }

    // ── getCountryDetail ──────────────────────────────────────────────────────

    @Test
    fun `given cached detail when getCountryDetail then returns cached data without api call`() = runTest {
        val entity = CountryDetailEntity(
            cca3 = "BRA", commonName = "Brazil", officialName = "Federal Republic of Brazil",
            capital = "Brasília", flagUrl = "", region = "Americas", subregion = "South America",
            languages = listOf("Portuguese"), population = 215_000_000L,
            borders = listOf("ARG"), currencies = listOf("Brazilian real"), independent = true
        )
        coEvery { countryDetailDao.getByCode("BRA") } returns entity

        val result = repository.getCountryDetail("BRA")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.commonName).isEqualTo("Brazil")
        coVerify(exactly = 0) { api.getCountryDetail(any()) }
    }

    @Test
    fun `given no cached detail when getCountryDetail then fetches from api`() = runTest {
        coEvery { countryDetailDao.getByCode("BRA") } returns null
        coEvery { api.getCountryDetail("BRA") } returns BaseResponse<CountryRemote>(
            DataWrapper(
                listOf(
                    CountryRemote(
                        codes = CodesRemote("BRA"),
                        names = NameRemote("Brazil", "Federal Republic"),
                        capitals = listOf(CapitalRemote("Brasília")),
                        flag = FlagRemote("url", null),
                        region = "Americas",
                        subregion = "South America",
                        languages = null,
                        population = 215_000_000L,
                        borders = listOf("ARG"),
                        currencies = null,
                        classification = ClassificationRemote(dependency = false)
                    )
                )
            )
        )

        val result = repository.getCountryDetail("BRA")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.cca3).isEqualTo("BRA")
        coVerify(exactly = 1) { countryDetailDao.insert(any()) }
    }

    @Test
    fun `given country not in api response when getCountryDetail then returns failure`() = runTest {
        coEvery { countryDetailDao.getByCode("XYZ") } returns null
        coEvery { api.getCountryDetail("XYZ") } returns BaseResponse<CountryRemote>(DataWrapper(emptyList()))

        val result = repository.getCountryDetail("XYZ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("XYZ")
    }

    // ── searchCountries ───────────────────────────────────────────────────────

    @Test
    fun `given empty query when searchCountries then returns all from dao`() = runTest {
        val entity = CountryEntity(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas", independent = true)
        coEvery { countryDao.getAllCountries() } returns listOf(entity)

        val result = repository.searchCountries("")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(1)
        coVerify(exactly = 1) { countryDao.getAllCountries() }
        coVerify(exactly = 0) { countryDao.searchCountries(any()) }
    }

    @Test
    fun `given query when searchCountries then calls searchCountries on dao`() = runTest {
        val entity = CountryEntity(cca3 = "BRA", commonName = "Brazil", capital = "Brasília", flagUrl = "", region = "Americas", independent = true)
        coEvery { countryDao.searchCountries("bra") } returns listOf(entity)

        val result = repository.searchCountries("bra")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(1)
        coVerify(exactly = 1) { countryDao.searchCountries("bra") }
    }
}
