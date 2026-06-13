package dev.gustavo.countries.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.gustavo.countries.data.remote.api.CountryApiService
import dev.gustavo.countries.data.remote.model.toDomain
import dev.gustavo.countries.domain.model.Country

class CountryPagingSource(
    private val api: CountryApiService,
    private val query: String? = null
) : PagingSource<Int, Country>() {

    override fun getRefreshKey(state: PagingState<Int, Country>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(PAGE_SIZE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(PAGE_SIZE)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        val offset = params.key ?: 0
        return try {
            val response = api.getAllCountries(
                query = query,
                limit = params.loadSize,
                offset = offset
            )
            
            val countries = response.data?.objects
                ?.map { it.toDomain() }
                ?.filter { it.cca3.isNotBlank() }
                ?: emptyList()
            
            val nextOffset = if (response.data?.meta?.more == true) {
                offset + params.loadSize
            } else {
                null
            }

            LoadResult.Page(
                data = countries,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = nextOffset
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 25
    }
}
