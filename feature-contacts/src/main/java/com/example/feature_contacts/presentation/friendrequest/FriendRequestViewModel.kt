package com.example.feature_contacts.presentation.friendrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_model.FriendRequest
import com.example.core_ui.state.UiState
import com.example.feature_contacts.domain.usecase.AcceptFriendRequestUseCase
import com.example.feature_contacts.domain.usecase.ObserveFriendRequestsByReceiverIdUseCase
import com.example.feature_contacts.domain.usecase.RejectFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val rejectFriendRequestUseCase: RejectFriendRequestUseCase,
    private val observeFriendRequestsByReceiverIdUseCase: ObserveFriendRequestsByReceiverIdUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FriendRequestUiState>> (
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<FriendRequestEvent>()
    val event = _event.asSharedFlow()

    fun loadFriendRequests() {
        viewModelScope.launch {
            observeFriendRequestsByReceiverIdUseCase()
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { requests ->
                    _uiState.value = if(requests.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(
                            FriendRequestUiState(requests)
                        )
                    }
                }
        }
    }

    fun acceptFriendRequest(request: FriendRequest) {
        viewModelScope.launch {
            acceptFriendRequestUseCase(request)
                .onFailure {
                    _event.emit(FriendRequestEvent.ShowError)
                }
        }
    }

    fun rejectFriendRequest(requestId: String) {
        viewModelScope.launch {
            rejectFriendRequestUseCase(requestId)
                .onFailure {
                    _event.emit(FriendRequestEvent.ShowError)
                }
        }
    }
}