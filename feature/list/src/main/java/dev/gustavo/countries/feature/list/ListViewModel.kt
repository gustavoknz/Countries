package dev.gustavo.countries.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow<ListViewState>(ListViewState.Loading)
    val viewState: StateFlow<ListViewState> = _viewState.asStateFlow()

    private val _events = MutableSharedFlow<ListEvent>()
    val events: SharedFlow<ListEvent> = _events.asSharedFlow()

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.LoadCountries -> loadCountries()
            is ListAction.SearchQueryChanged -> search(action.query)
            is ListAction.CountryClicked -> navigateToDetail(action.cca3)
        }
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _viewState.value = ListViewState.Loading
            getCountriesUseCase()
                .onSuccess { countries ->
                    _viewState.value = ListViewState.Loaded(countries.toImmutableList())
                }
                .onFailure { error ->
                    _viewState.value = ListViewState.Error(error.message ?: "Unknown error")
                }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            searchCountriesUseCase(query)
                .onSuccess { countries ->
                    _viewState.value = ListViewState.Loaded(
                        countries = countries.toImmutableList(),
                        searchQuery = query
                    )
                }
                .onFailure { error ->
                    _viewState.value = ListViewState.Error(error.message ?: "Unknown error")
                }
        }
    }

    private fun navigateToDetail(cca3: String) {
        viewModelScope.launch {
            _events.emit(ListEvent.NavigateToDetail(cca3))
        }
    }
}
