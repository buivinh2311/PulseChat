package com.example.feature_contacts.presentation.sentfriendrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_model.FriendRequest
import com.example.core_ui.state.UiState
import com.example.feature_contacts.usecase.ObserveFriendRequestsBySenderIdUseCase
import com.example.feature_contacts.usecase.RecallFriendRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentFriendRequestViewModel @Inject constructor(
    private val recallFriendRequestUseCase: RecallFriendRequestUseCase,
    private val observeFriendRequestsBySenderIdUseCase: ObserveFriendRequestsBySenderIdUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<SentFriendRequestUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SentFriendRequestEvent>()
    val event = _event.asSharedFlow()

    fun loadSentFriendRequest() {
        viewModelScope.launch {
            observeFriendRequestsBySenderIdUseCase()
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { requests ->
                    _uiState.value = if(requests.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(
                            SentFriendRequestUiState(requests)
                        )
                    }
                }
        }
    }

    fun recallFriendRequest(request: FriendRequest) {
        viewModelScope.launch {
            recallFriendRequestUseCase(request)
                .onFailure {
                    _event.emit(SentFriendRequestEvent.ShowError)
                }
        }
    }
}