package dev.gustavo.countries.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.core.common.toDataError
import dev.gustavo.countries.core.ui.util.toUiText
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase
import dev.gustavo.countries.feature.detail.model.toUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCountryDetailUseCase: GetCountryDetailUseCase
) : ViewModel() {

    private val _loadTrigger = MutableSharedFlow<DetailAction.LoadDetail>(replay = 1)

    val viewState: StateFlow<DetailViewState> = _loadTrigger
        .flatMapLatest { loadAction ->
            flow {
                emit(DetailViewState.Loading(cca3 = loadAction.cca3, flagUrl = loadAction.flagUrl))

                getCountryDetailUseCase(loadAction.cca3)
                    .onSuccess { detail ->
                        emit(DetailViewState.Loaded(detail.toUiModel()))
                    }
                    .onFailure { error ->
                        emit(
                            DetailViewState.Error(
                                message = error.toDataError().toUiText(),
                                countryCode = loadAction.cca3
                            )
                        )
                    }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MS),
            initialValue = DetailViewState.Loading()
        )

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.LoadDetail -> {
                viewModelScope.launch {
                    _loadTrigger.emit(action)
                }
            }

            is DetailAction.BackClicked -> navigateBack()
            is DetailAction.BorderClicked -> navigateToDetail(action.cca3)
        }
    }

    private fun navigateToDetail(cca3: String) {
        viewModelScope.launch {
            _events.emit(DetailEvent.NavigateToDetail(cca3))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.emit(DetailEvent.NavigateBack)
        }
    }

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MS = 5000L
    }
}
