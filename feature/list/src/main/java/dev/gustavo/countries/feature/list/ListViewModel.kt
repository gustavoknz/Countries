package dev.gustavo.countries.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import dev.gustavo.countries.core.common.Constants
import dev.gustavo.countries.core.common.ConnectivityObserver
import dev.gustavo.countries.feature.list.model.toUiModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _viewState = MutableStateFlow<ListViewState>(ListViewState.Loading())
    val viewState: StateFlow<ListViewState> = _viewState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _events = MutableSharedFlow<ListEvent>()
    val events: SharedFlow<ListEvent> = _events.asSharedFlow()

    private var searchJob: Job? = null

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityObserver.status
            .onEach { status ->
                val isOffline = status != ConnectivityObserver.Status.Available
                updateOfflineStatus(isOffline)
            }
            .launchIn(viewModelScope)
    }

    private fun updateOfflineStatus(isOffline: Boolean) {
        val currentState = _viewState.value
        _viewState.value = when (currentState) {
            is ListViewState.Loading -> currentState.copy(isOffline = isOffline)
            is ListViewState.Loaded -> currentState.copy(isOffline = isOffline)
            is ListViewState.Error -> currentState.copy(isOffline = isOffline)
        }
    }

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.LoadCountries -> loadCountries()
            is ListAction.SearchQueryChanged -> {
                _searchQuery.value = action.query
                search(action.query)
            }
            is ListAction.SearchTriggered -> search(query = _searchQuery.value, debounce = false)
            is ListAction.Refresh -> refresh()
            is ListAction.CountryClicked -> navigateToDetail(action.cca3)
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            val isOffline = _viewState.value.isOffline
            _viewState.value = ListViewState.Loading(isOffline = isOffline)
            getCountriesUseCase()
                .onSuccess { countries ->
                    _viewState.value = ListViewState.Loaded(
                        countries = countries.map { it.toUiModel() }.toImmutableList(),
                        isOffline = isOffline
                    )
                }
                .onFailure { error ->
                    if (error.message != null) {
                        println("Error fetching all countries: ${error.message!!}")
                    }
                    _viewState.value = ListViewState.Error(
                        message = error.message ?: "Unknown error",
                        isOffline = isOffline
                    )
                }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            val currentState = _viewState.value
            val isOffline = currentState.isOffline
            when (currentState) {
                is ListViewState.Loaded -> _viewState.value = currentState.copy(isRefreshing = true)
                is ListViewState.Error -> _viewState.value = currentState.copy(isRefreshing = true)
                else -> Unit
            }
            
            val query = _searchQuery.value
            val result = if (query.isBlank()) {
                getCountriesUseCase(forceRefresh = true)
            } else {
                searchCountriesUseCase(query, forceRefresh = true)
            }

            result.onSuccess { countries ->
                _viewState.value = ListViewState.Loaded(
                    countries = countries.map { it.toUiModel() }.toImmutableList(),
                    isRefreshing = false,
                    isOffline = isOffline
                )
            }.onFailure { error ->
                val errorMessage = error.message ?: "Unknown error"
                val finalState = when (currentState) {
                    is ListViewState.Loaded -> currentState.copy(isRefreshing = false)
                    is ListViewState.Error -> currentState.copy(isRefreshing = false)
                    else -> ListViewState.Error(message = errorMessage, isOffline = isOffline)
                }
                
                if (currentState is ListViewState.Loaded || currentState is ListViewState.Error) {
                    _events.emit(ListEvent.ShowError(errorMessage))
                }
                
                _viewState.value = finalState
            }
        }
    }

    private fun search(query: String, debounce: Boolean = true) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (debounce) {
                delay(Constants.SEARCH_DEBOUNCE_DELAY_MS.milliseconds)
            }
            val isOffline = _viewState.value.isOffline
            searchCountriesUseCase(query)
                .onSuccess { countries ->
                    _viewState.value = ListViewState.Loaded(
                        countries = countries.map { it.toUiModel() }.toImmutableList(),
                        isOffline = isOffline
                    )
                }
                .onFailure { error ->
                    _viewState.value = ListViewState.Error(
                        message = error.message ?: "Unknown error",
                        isOffline = isOffline
                    )
                }
        }
    }

    private fun navigateToDetail(cca3: String) {
        viewModelScope.launch {
            _events.emit(ListEvent.NavigateToDetail(cca3))
        }
    }
}
