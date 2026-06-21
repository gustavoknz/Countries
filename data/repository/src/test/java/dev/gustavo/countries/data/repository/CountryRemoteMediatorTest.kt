package dev.gustavo.countries.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.data.local.dao.CountryDao
import dev.gustavo.countries.data.local.dao.RemoteKeyDao
import dev.gustavo.countries.data.local.database.CountriesDatabase
import dev.gustavo.countries.data.local.entity.CountryEntity
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.BaseResponse
import dev.gustavo.countries.data.remote.model.CountryRemote
import dev.gustavo.countries.data.remote.model.DataWrapper
import dev.gustavo.countries.data.remote.model.MetaRemote
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
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
        coEvery { database.withTransaction<Any>(any()) } coAnswers {
            val block = it.invocation.args[1] as suspend () -> Any
            block()
        }
        
        mediator = CountryRemoteMediator(api, database, null)
    }

    @Test
    fun `given success response when load REFRESH then returns Success and endOfPagination is false`() = runTest {
        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 100, count = 0, limit = 25, offset = 0, more = true)
            )
        )
        coEvery { api.getAllCountries(any(), any(), any(), any()) } returns response

        val pagingState = PagingState<Int, CountryEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 25),
            leadingPlaceholderCount = 0
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        if (result is RemoteMediator.MediatorResult.Error) {
            throw result.throwable
        }

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun `given success response with no more when load REFRESH then returns Success and endOfPagination is true`() = runTest {
        val response = BaseResponse<CountryRemote>(
            DataWrapper(
                objects = emptyList(),
                meta = MetaRemote(total = 10, count = 10, limit = 25, offset = 0, more = false)
            )
        )
        coEvery { api.getAllCountries(any(), any(), any(), any()) } returns response

        val pagingState = PagingState<Int, CountryEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 25),
            leadingPlaceholderCount = 0
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        if (result is RemoteMediator.MediatorResult.Error) {
            throw result.throwable
        }

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun `given error response when load REFRESH then returns Error`() = runTest {
        val exception = RuntimeException("API Error")
        coEvery { api.getAllCountries(any(), any(), any(), any()) } throws exception

        val pagingState = PagingState<Int, CountryEntity>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 25),
            leadingPlaceholderCount = 0
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
        assertThat((result as RemoteMediator.MediatorResult.Error).throwable).isEqualTo(exception)
    }
}
