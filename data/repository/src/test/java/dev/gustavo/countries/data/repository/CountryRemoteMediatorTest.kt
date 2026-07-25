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
import java.util.concurrent.TimeUnit

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
        val blockSlot = slot<suspend () -> Any>()
        coEvery { database.withTransaction(capture(blockSlot)) } coAnswers { blockSlot.captured() }
    }

    @Test
    fun `when initialize and no remote key exists then returns LAUNCH_INITIAL_REFRESH`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns null
            assertThat(mediator.initialize()).isEqualTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
        }
    }

    @Test
    fun `when initialize and remote key is expired then returns LAUNCH_INITIAL_REFRESH`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            val expiredTime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", null, expiredTime)
            assertThat(mediator.initialize()).isEqualTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
        }
    }

    @Test
    fun `when initialize and remote key is fresh then returns SKIP_INITIAL_REFRESH`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            val freshTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30)
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", null, freshTime)
            assertThat(mediator.initialize()).isEqualTo(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
        }
    }

    @Test
    fun `given success response when load REFRESH then returns Success`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns createResponse(more = true)
            val result = mediator.load(LoadType.REFRESH, createPagingState())
            assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
            assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
            coVerify { countryDao.clearSearchCache(Constants.MAIN_LIST_QUERY_ID) }
        }
    }

    @Test
    fun `given search query when load REFRESH then clears search results`() {
        runTest {
            val query = "bra"
            mediator = CountryRemoteMediator(api, database, CountryQuery(query))
            coEvery { api.getAllCountries(query, any(), any(), any(), any()) } returns createResponse()
            mediator.load(LoadType.REFRESH, createPagingState())
            coVerify { countryDao.clearSearchCache(query) }
        }
    }

    @Test
    fun `given region filter when load REFRESH then calls api with region`() {
        runTest {
            val region = Region.AMERICAS
            mediator = CountryRemoteMediator(api, database, CountryQuery(null, region))
            coEvery { api.getAllCountries(null, region.apiValue, any(), any(), any()) } returns createResponse()
            mediator.load(LoadType.REFRESH, createPagingState())
            coVerify { api.getAllCountries(null, region.apiValue, any(), any(), any()) }
        }
    }

    @Test
    fun `when load PREPEND then returns Success and endOfPagination is true`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            val result = mediator.load(LoadType.PREPEND, createPagingState())
            assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
        }
    }

    @Test
    fun `when load APPEND and no remote key then returns Success`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns null
            val result = mediator.load(LoadType.APPEND, createPagingState()) as RemoteMediator.MediatorResult.Success
            assertThat(result.endOfPaginationReached).isTrue()
        }
    }

    @Test
    fun `when load APPEND and remote key exists then fetches next page`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", 25)
            coEvery { api.getAllCountries(any(), any(), any(), 25, any()) } returns createResponse()
            val result = mediator.load(LoadType.APPEND, createPagingState())
            assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        }
    }

    @Test
    fun `when load APPEND and no nextKey then returns Success`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { remoteKeyDao.getRemoteKeyById(any()) } returns RemoteKeyEntity("id", null)
            val result = mediator.load(LoadType.APPEND, createPagingState()) as RemoteMediator.MediatorResult.Success
            assertThat(result.endOfPaginationReached).isTrue()
        }
    }

    @Test
    fun `given response with invalid objects when load then filters them out`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            val countries = listOf(TestData.createCountryRemote(""), TestData.createCountryRemote("BRA"))
            val response = BaseResponse(DataWrapper(countries, MetaRemote(100, 2, 25, 0, false)))
            coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns response
            mediator.load(LoadType.REFRESH, createPagingState())
            val captured = slot<List<CountryEntity>>()
            coVerify { countryDao.insertAll(capture(captured)) }
            assertThat(captured.captured).hasSize(1)
        }
    }

    @Test
    fun `given null response data when load then returns endOfPagination true`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } returns BaseResponse(null)
            val result = mediator.load(LoadType.REFRESH, createPagingState()) as RemoteMediator.MediatorResult.Success
            assertThat(result.endOfPaginationReached).isTrue()
        }
    }

    @Test
    fun `given error response when load then returns Error`() {
        runTest {
            mediator = CountryRemoteMediator(api, database, CountryQuery(null))
            val exception = RuntimeException("API Error")
            coEvery { api.getAllCountries(any(), any(), any(), any(), any()) } throws exception
            val result = mediator.load(LoadType.REFRESH, createPagingState()) as RemoteMediator.MediatorResult.Error
            assertThat(result.throwable).isEqualTo(exception)
        }
    }

    private fun createPagingState() = PagingState<Int, CountryEntity>(emptyList(), null, PagingConfig(25), 0)

    private fun createResponse(more: Boolean = false) =
        BaseResponse<CountryRemote>(DataWrapper(emptyList(), MetaRemote(100, 0, 25, 0, more)))
}
