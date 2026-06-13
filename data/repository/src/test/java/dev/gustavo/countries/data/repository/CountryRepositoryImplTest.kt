package dev.gustavo.countries.data.repository

import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CapitalRemote
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.MetaRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    fun `when getCountries then returns flow of paging data`() = runTest {
        val result = repository.getCountries()
        assertThat(result).isNotNull()
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
                objects = listOf(
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
                ),
                meta = MetaRemote(total = 1, count = 1, limit = 1, offset = 0, more = false)
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
        coEvery { api.getCountryDetail("XYZ") } returns BaseResponse<CountryRemote>(
            DataWrapper(objects = emptyList(), meta = null)
        )

        val result = repository.getCountryDetail("XYZ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("XYZ")
    }

    // ── searchCountries ───────────────────────────────────────────────────────
    // searchCountries is still used in Some places, but RepositoryImpl might have it deprecated or simplified
}
