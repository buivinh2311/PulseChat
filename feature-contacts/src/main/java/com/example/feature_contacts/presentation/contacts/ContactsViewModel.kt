package com.example.feature_contacts.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.GetCurrentUserIdUseCase
import com.example.core_domain.usecase.GetFriendsUseCase
import com.example.core_ui.state.UiState
import com.example.core_domain.usecase.GetGroupsUseCase
import com.example.core_domain.usecase.ObserveOnlineStatusUseCase
import com.example.core_model.Group
import com.example.core_model.User
import com.example.core_model.composite.ConversationDisplayInfo
import com.example.core_model.composite.ConversationWithInfo
import com.example.feature_contacts.domain.model.FriendWithOnlineStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val observeOnlineStatusUseCase: ObserveOnlineStatusUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<ContactsUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ContactsEvent>()
    val event = _event.asSharedFlow()

    fun loadData() {
        viewModelScope.launch {
            val currentUserId = getCurrentUserIdUseCase()
                .getOrElse {
                    _uiState.value = UiState.Error
                    return@launch
                }

            val groups = getGroupsUseCase()
                .getOrElse {
                    _uiState.value = UiState.Error
                    return@launch
                }

            observeFriendPresence(
                getFriendsUseCase()
                    .getOrElse {
                        _uiState.value = UiState.Error
                        return@launch
                    }
            ).collect { friendWithOnlineStatuses ->
                _uiState.value = UiState.Success(
                    ContactsUiState(
                        currentUserId = currentUserId,
                        friends = friendWithOnlineStatuses,
                        groups = groups
                    )
                )
            }
        }
    }

    private fun observeFriendPresence(
        friends: List<User>
    ): Flow<List<FriendWithOnlineStatus>> {
        if (friends.isEmpty()) return flowOf(emptyList())
        return combine(
            friends.map { friend ->
                observeOnlineStatusUseCase(friend.id)
                    .map { isOnline ->
                        FriendWithOnlineStatus(
                            friend = friend,
                            isOnline = isOnline
                        )
                    }
            }
        ) { results ->
            results.toList()
        }
    }
}