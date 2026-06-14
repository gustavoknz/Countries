package dev.gustavo.countries.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.core.common.toDataError
import dev.gustavo.countries.core.ui.util.toUiText
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase
import dev.gustavo.countries.feature.detail.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCountryDetailUseCase: GetCountryDetailUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    val viewState: StateFlow<DetailViewState> = _viewState.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    private var loadJob: Job? = null

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.LoadDetail -> loadDetail(action.cca3)
            is DetailAction.BackClicked -> navigateBack()
        }
    }

    private fun loadDetail(cca3: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _viewState.value = DetailViewState.Loading
            getCountryDetailUseCase(cca3)
                .onSuccess { detail ->
                    _viewState.value = DetailViewState.Loaded(detail.toUiModel())
                }
                .onFailure { error ->
                    _viewState.value =
                        DetailViewState.Error(message = error.toDataError().toUiText(), countryCode = cca3)
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.emit(DetailEvent.NavigateBack)
        }
    }
}
