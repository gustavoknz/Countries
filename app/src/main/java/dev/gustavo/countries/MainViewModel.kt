package dev.gustavo.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gustavo.countries.core.common.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _isDismissed = MutableStateFlow(value = false)

    val showConnectivitySnackbar: StateFlow<Boolean> = connectivityObserver.status
        .onEach { status ->
            // Reset dismissed state when we come back online
            if (status == ConnectivityObserver.Status.Available) {
                _isDismissed.value = false
            }
        }
        .map { it != ConnectivityObserver.Status.Available }
        .combine(_isDismissed) { isOffline, dismissed ->
            isOffline && !dismissed
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MS),
            initialValue = false
        )

    fun dismissSnackbar() {
        _isDismissed.value = true
    }

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MS = 5000L
    }
}
