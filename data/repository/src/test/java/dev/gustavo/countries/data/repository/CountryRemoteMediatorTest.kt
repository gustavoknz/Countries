package dev.gustavo.countries.data.repository

import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.core.common.Region
import dev.gustavo.countries.core.testing.TestData
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.local.entity.CountrySearchResultEntity
import dev.gustavo.countries.data.local.entity.RemoteKeyEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.MetaRemote
import dev.gustavo.countries.domain.model.CountryQuery
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CountryRemoteMediatorTest {

    private val api: CountryApiService = mockk()
    private val database: CountriesDatabase = mockk()
    private val countryDao: CountryDao = mockk(relaxed = true)
    private val remoteKeyDao: RemoteKeyDao = mockk(relaxed = true)

    private lateinit var mediator: CountryRemoteMediator

    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        every { database.countryDao() } returns countryDao
        every { database.remoteKeyDao() } returns remoteKeyDao

        // Mock withTransaction for any return type
        val blockSlot = slot<suspend () -> Any>()
        coEvery { database.withTransaction(capture(blockSlot)) } coAnswers {
            blockSlot.captured()
        }
    }

    @Test
    fun `given success response when load REFRESH then returns Success and endOfPagination is false`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 100, count = 0, limit = 25, offset = 0, more = true)
            )
        )
        coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns response

        val result = mediator.load(LoadType.REFRESH, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()

        coVerify { countryDao.clearSearchCache(Constants.MAIN_LIST_QUERY_ID) }
        coVerify { remoteKeyDao.clearSearchCache(RemoteKeyEntity.COUNTRIES_LIST_ID) }
        coVerify { countryDao.insertAll(any()) }
        coVerify { countryDao.insertSearchResults(any()) }
    }

    @Test
    fun `given search query when load REFRESH then clears search results`() = runTest {
        val query = "bra"
        mediator = CountryRemoteMediator(api, database, CountryQuery(query))
        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 10, count = 0, limit = 25, offset = 0, more = false)
            )
        )
        coEvery { api.getAllCountries(query, any(), any(), any(), any()) } returns response

        mediator.load(LoadType.REFRESH, createPagingState())

        val expectedRemoteKeyId = RemoteKeyEntity.getListId(query, null)
        coVerify { countryDao.clearSearchCache(query) }
        coVerify { remoteKeyDao.clearSearchCache(expectedRemoteKeyId) }
    }

    @Test
    fun `given region filter when load REFRESH then calls api with region and clears search results`() = runTest {
        val region = Region.AMERICAS
        mediator = CountryRemoteMediator(api, database, CountryQuery(null, region))
        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 10, count = 0, limit = 25, offset = 0, more = false)
            )
        )
        coEvery { api.getAllCountries(null, region.apiValue, any(), any(), any()) } returns response

        mediator.load(LoadType.REFRESH, createPagingState())

        val expectedKeyId = RemoteKeyEntity.getListId(null, region)
        coVerify { api.getAllCountries(null, region.apiValue, any(), any(), any()) }
        coVerify { countryDao.clearSearchCache(Constants.MAIN_LIST_QUERY_ID) }
        coVerify { remoteKeyDao.clearSearchCache(expectedKeyId) }
    }

    @Test
    fun `when load PREPEND then returns Success and endOfPagination is true`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        val result = mediator.load(LoadType.PREPEND, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
        coVerify(exactly = 0) { api.getAllCountries(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `when load APPEND and no remote key then returns Success and endOfPagination is true`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns null

        val result = mediator.load(LoadType.APPEND, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `when load APPEND and remote key has no nextKey then returns Success and endOfPagination is true`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", null)

        val result = mediator.load(LoadType.APPEND, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `when load APPEND and remote key exists then fetches next page`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        val nextOffset = 25
        coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", nextOffset)

        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 100, count = 0, limit = 25, offset = 25, more = true)
            )
        )
        coEvery { api.getAllCountries(any(), any(), any(), nextOffset, any()) } returns response

        val result = mediator.load(LoadType.APPEND, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
        coVerify { api.getAllCountries(null, any(), any(), nextOffset, any()) }
    }

    @Test
    fun `given response with invalid objects when load then filters them out`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        val response = BaseResponse(
            DataWrapper(
                objects = listOf(
                    TestData.createCountryRemote(cca3 = ""),
                    TestData.createCountryRemote(cca3 = TestData.COUNTRY_CODE_BRA)
                ),
                meta = MetaRemote(total = 100, count = 2, limit = 25, offset = 0, more = false)
            )
        )
        coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns response

        mediator.load(LoadType.REFRESH, createPagingState())

        val capturedCountries = slot<List<CountryEntity>>()
        coVerify { countryDao.insertAll(capture(capturedCountries)) }

        val capturedResults = slot<List<CountrySearchResultEntity>>()
        coVerify { countryDao.insertSearchResults(capture(capturedResults)) }

        assertThat(capturedCountries.captured).hasSize(1)
        assertThat(capturedCountries.captured[0].cca3).isEqualTo(TestData.COUNTRY_CODE_BRA)
        
        assertThat(capturedResults.captured).hasSize(1)
        assertThat(capturedResults.captured[0].cca3).isEqualTo(TestData.COUNTRY_CODE_BRA)
        assertThat(capturedResults.captured[0].queryId).isEqualTo(Constants.MAIN_LIST_QUERY_ID)
    }

    @Test
    fun `given null response data when load then returns endOfPagination true`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns BaseResponse(null)

        val result = mediator.load(LoadType.REFRESH, createPagingState())

        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
        coVerify { countryDao.insertAll(emptyList()) }
        coVerify { countryDao.insertSearchResults(emptyList()) }
    }

    @Test
    fun `given error response when load then returns Error`() = runTest {
        mediator = CountryRemoteMediator(api, database, CountryQuery(null))
        val exception = RuntimeException("API Error")
        coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } throws exception

        val result = mediator.load(LoadType.REFRESH, createPagingState())

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Error).throwable).isEqualTo(exception)
    }

    private fun createPagingState() = PagingState<Int, CountryEntity>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 25),
        leadingPlaceholderCount = 0
    )
}
