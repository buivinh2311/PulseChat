package com.example.feature_conversation.presentation.conversationoption

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.GetCurrentUserIdUseCase
import com.example.core_model.Group
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.usecase.ChangeGroupAvatarUseCase
import com.example.feature_conversation.domain.usecase.ChangeGroupNameUseCase
import com.example.feature_conversation.domain.usecase.ObserveConversationByIdUseCase
import com.example.feature_conversation.domain.usecase.PinConversationUseCase
import com.example.feature_conversation.domain.usecase.UnpinConversationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConversationOptionViewModel @Inject constructor(
    private val observeConversationByIdUseCase: ObserveConversationByIdUseCase,
    private val changeGroupAvatarUseCase: ChangeGroupAvatarUseCase,
    private val changeGroupNameUseCase: ChangeGroupNameUseCase,
    private val pinConversationUseCase: PinConversationUseCase,
    private val unpinConversationUseCase: UnpinConversationUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ConversationOptionUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ConversationOptionEvent>()
    val event = _event.asSharedFlow()

    fun loadConversation(conversationId: String) {
        val currentUserId = getCurrentUserIdUseCase()
            .getOrElse {
                _uiState.value = UiState.Error
                return
            }
        viewModelScope.launch {
            observeConversationByIdUseCase(conversationId)
                .catch {
                    _event.emit(ConversationOptionEvent.ShowError)
                }
                .collect { data ->
                    _uiState.value = UiState.Success(
                        ConversationOptionUiState(
                            conversationDetailData = data,
                            currentUserId = currentUserId
                        )
                    )
                }
        }
    }

    fun changeGroupAvatar(
        group: Group,
        file: File
    ) {
        viewModelScope.launch {
            changeGroupAvatarUseCase(group, file)
                .onFailure {
                    _event.emit(ConversationOptionEvent.ShowError)
                }
        }
    }

    fun changeGroupName(
        group: Group,
        groupName: String
    ) {
        viewModelScope.launch {
            changeGroupNameUseCase(group, groupName)
                .onFailure {
                    _event.emit(ConversationOptionEvent.ShowError)
                }
        }
    }

    fun pinConversation(conversationId: String) {
        viewModelScope.launch {
            pinConversationUseCase(conversationId)
                .onFailure {
                    _event.emit(ConversationOptionEvent.ShowError)
                }
        }
    }

    fun unpinConversation(conversationId: String) {
        viewModelScope.launch {
            unpinConversationUseCase(conversationId)
                .onFailure {
                    _event.emit(ConversationOptionEvent.ShowError)
                }
        }
    }
}