package com.example.feature_conversation.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.GetCurrentUserIdUseCase
import com.example.core_domain.usecase.ObserveOnlineStatusUseCase
import com.example.core_model.Conversation
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_model.composite.ConversationWithInfo
import com.example.core_model.enum.ConversationType
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.usecase.ObserveConversationsWithInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeConversationsWithInfoUseCase: ObserveConversationsWithInfoUseCase,
    private val observeOnlineStatusUseCase: ObserveOnlineStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ConversationUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ConversationEvent>()
    val event = _event.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadConversations() {
        viewModelScope.launch {
            val currentUserId = getCurrentUserIdUseCase()
                .getOrElse { return@launch }

            observeConversationsWithInfoUseCase()
                .flatMapLatest { conversations ->
                    observeConversationsWithPresence(currentUserId, conversations)
                }
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { conversationWithInfos ->
                    _uiState.value = if (conversationWithInfos.isEmpty()) {
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

    private fun observeConversationsWithPresence(
        currentUserId: String,
        infos: List<ConversationWithInfo>
    ): Flow<List<ConversationWithInfo>> {
        if (infos.isEmpty()) return flowOf(emptyList())
        return combine(
            infos.map { info ->
                observeConversationPresence(currentUserId,info)
            }
        ) { results ->
            results.toList()
        }
    }

    private fun observeConversationPresence(
        currentUserId: String,
        info: ConversationWithInfo
    ): Flow<ConversationWithInfo> {
        return when (val displayInfo = info.displayInfo) {
            is ConversationDisplayInfo.Direct -> {
                observeOnlineStatusUseCase(displayInfo.otherUser.id)
                    .map { isOnline ->
                        info.copy(isOnline = isOnline)
                    }
            }

            is ConversationDisplayInfo.Group -> {
                val memberIds = info.conversation.participants
                    .filterNot { it == currentUserId }

                if (memberIds.isEmpty()) {
                    flowOf(info.copy(isOnline = false))
                } else {
                    combine(
                        memberIds.map { userId ->
                            observeOnlineStatusUseCase(userId)
                        }
                    ) { statuses ->
                        info.copy(isOnline = statuses.any { it })
                    }
                }
            }
        }
    }
}