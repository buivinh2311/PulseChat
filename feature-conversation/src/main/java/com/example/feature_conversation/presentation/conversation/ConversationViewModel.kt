package com.example.feature_conversation.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.GetCurrentUserIdUseCase
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.usecase.ObserveConversationsWithInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeConversationsWithInfoUseCase: ObserveConversationsWithInfoUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ConversationUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ConversationEvent>()
    val event = _event.asSharedFlow()

    fun loadConversations() {
        viewModelScope.launch {
            val currentUserId = getCurrentUserIdUseCase()
                .getOrElse { return@launch }

            observeConversationsWithInfoUseCase()
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { conversationWithInfos ->
                    _uiState.value = if(conversationWithInfos.isEmpty()) {
                        UiState.Empty
                    } else {
                        UiState.Success(
                            ConversationUiState(
                                currentUserId = currentUserId,
                                conversationWithInfos = conversationWithInfos
                            )
                        )
                    }
                }
        }
    }
}