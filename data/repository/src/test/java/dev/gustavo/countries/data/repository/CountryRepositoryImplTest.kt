package dev.gustavo.countries.data.repository

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.common.CountryNotFoundException
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.CountryDetailDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.CountryDetailEntity
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.ClassificationRemote
import dev.gustavo.countries.data.remote.model.CodesRemote
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.FlagRemote
import dev.gustavo.countries.data.remote.model.MetaRemote
import dev.gustavo.countries.data.remote.model.NameRemote
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
    private val database: CountriesDatabase = mockk()
    private val countryDao: CountryDao = mockk(relaxed = true)
    private val countryDetailDao: CountryDetailDao = mockk(relaxed = true)
    private val remoteKeyDao: RemoteKeyDao = mockk(relaxed = true)
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
        every { database.countryDao() } returns countryDao
        every { database.countryDetailDao() } returns countryDetailDao
        every { database.remoteKeyDao() } returns remoteKeyDao
        repository = CountryRepositoryImpl(api, database, countryDao, countryDetailDao, fakeDispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── getCountries ──────────────────────────────────────────────────────────

    @Test
    fun `given null query when getCountries and collected then calls getAllCountriesPaging`() = runTest {
        val pagingSource: PagingSource<Int, CountryEntity> = mockk(relaxed = true)
        every { countryDao.getAllCountriesPaging() } returns pagingSource

        val result = repository.getCountries(null)
        assertThat(result).isNotNull()

        // Collecting the flow to trigger Pager's pagingSourceFactory
        result.first()
        coVerify { countryDao.getAllCountriesPaging() }
    }

    @Test
    fun `given valid query when getCountries and collected then calls searchCountriesPaging`() = runTest {
        val query = "bra"
        val pagingSource: PagingSource<Int, CountryEntity> = mockk(relaxed = true)
        every { countryDao.searchCountriesPaging(query) } returns pagingSource

        val result = repository.getCountries(query)
        assertThat(result).isNotNull()

        result.first()
        coVerify { countryDao.searchCountriesPaging(query) }
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
        coEvery { api.getCountryDetail("BRA") } returns createDetailResponse("BRA")

        val result = repository.getCountryDetail("BRA")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.cca3).isEqualTo("BRA")
        coVerify(exactly = 1) { countryDetailDao.insert(any()) }
    }

    @Test
    fun `given country not in api response objects when getCountryDetail then returns failure`() = runTest {
        coEvery { countryDetailDao.getByCode("XYZ") } returns null
        coEvery { api.getCountryDetail("XYZ") } returns BaseResponse(
            DataWrapper(objects = emptyList(), meta = null)
        )

        val result = repository.getCountryDetail("XYZ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(CountryNotFoundException::class.java)
    }

    @Test
    fun `given response data is null when getCountryDetail then returns failure`() = runTest {
        coEvery { countryDetailDao.getByCode("XYZ") } returns null
        coEvery { api.getCountryDetail("XYZ") } returns BaseResponse(null)

        val result = repository.getCountryDetail("XYZ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(CountryNotFoundException::class.java)
    }

    @Test
    fun `given response detail has blank cca3 when getCountryDetail then returns failure`() = runTest {
        coEvery { countryDetailDao.getByCode("XYZ") } returns null
        coEvery { api.getCountryDetail("XYZ") } returns createDetailResponse("")

        val result = repository.getCountryDetail("XYZ")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(CountryNotFoundException::class.java)
    }

    @Test
    fun `given api error when getCountryDetail then returns failure`() = runTest {
        coEvery { countryDetailDao.getByCode("BRA") } returns null
        val exception = RuntimeException("Network Error")
        coEvery { api.getCountryDetail("BRA") } throws exception

        val result = repository.getCountryDetail("BRA")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    private fun createDetailResponse(cca3: String) = BaseResponse(
        DataWrapper(
            objects = listOf(
                CountryRemote(
                    codes = CodesRemote(cca3),
                    names = NameRemote("Name", "Official"),
                    capitals = emptyList(),
                    flag = FlagRemote("url", "url"),
                    region = "Region",
                    subregion = "Subregion",
                    languages = emptyList(),
                    population = 0L,
                    borders = emptyList(),
                    currencies = emptyList(),
                    classification = ClassificationRemote(dependency = false)
                )
            ),
            meta = MetaRemote(total = 1, count = 1, limit = 1, offset = 0, more = false)
        )
    )
}
