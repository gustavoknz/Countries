package dev.gustavo.countries.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.core.common.Constants.SEARCH_DEBOUNCE_DELAY_MS
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import dev.gustavo.countries.feature.list.model.UiCountry
import dev.gustavo.countries.feature.list.model.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ListViewModel @Inject constructor(
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val countries: Flow<PagingData<UiCountry>> = _searchQuery
        .debounce(SEARCH_DEBOUNCE_DELAY_MS.milliseconds)
        .flatMapLatest { query ->
            searchCountriesUseCase(query = query).map { pagingData ->
                pagingData.map { it.toUiModel() }
            }
        }
        .cachedIn(viewModelScope)

    private val _events = MutableSharedFlow<ListEvent>()
    val events: SharedFlow<ListEvent> = _events.asSharedFlow()

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver.status
            .onEach { status ->
                _isOffline.value = status != ConnectivityObserver.Status.Available
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.SearchQueryChanged -> _searchQuery.value = action.query
            is ListAction.CountryClicked -> navigateToDetail(action.cca3)
            else -> Unit
        }
    }

    private fun navigateToDetail(cca3: String) {
        viewModelScope.launch {
            _events.emit(ListEvent.NavigateToDetail(cca3))
        }
    }
}
