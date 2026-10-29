package com.example.feature_conversation.presentation.viewmember

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_ui.state.UiState
import com.example.core_domain.usecase.GetCurrentUserUseCase
import com.example.feature_conversation.domain.usecase.ObserveConversationByIdUseCase
import com.example.feature_conversation.presentation.conversationdetail.ConversationDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewMemberViewModel @Inject constructor(
    private val observeConversationByIdUseCase: ObserveConversationByIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ViewMemberUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase()
                .getOrElse {
                    _uiState.value = UiState.Error
                    return@launch
                }
            observeConversationByIdUseCase(conversationId)
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { data ->
                    when(data) {
                        is ConversationDetailData.Direct -> {
                            _uiState.value = UiState.Error
                        }

                        is ConversationDetailData.Group -> {
                            _uiState.value = UiState.Success(
                                ViewMemberUiState(
                                    conversationData = data,
                                    currentUserId = currentUser.id
                                )
                            )
                        }
                    }
                }
        }
    }
}