package com.example.feature_conversation.presentation.creategroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_model.User
import com.example.core_ui.state.UiState
import com.example.feature_conversation.domain.usecase.CreateGroupUseCase
import com.example.core_domain.usecase.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase,
    private val getFriendsUseCase: GetFriendsUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<CreateGroupUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CreateGroupEvent>()
    val event = _event.asSharedFlow()

    fun loadFriends(currentFriendId: String) {
        viewModelScope.launch {
            getFriendsUseCase()
                .onSuccess { friends ->
                    _uiState.value = UiState.Success(
                        CreateGroupUiState(
                            friends = friends,
                            groupMember = friends.filter { it.id == currentFriendId },
                            isGroupNameBlank = false
                        )
                    )
                }
                .onFailure {
                    _uiState.value = UiState.Error
                }
        }
    }

    fun searchFriend(
        keyword: String,
        friends: List<User>
    ) {
        viewModelScope.launch {
            val result = friends.filter {
                val displayName = it.fullName
                displayName.contains(keyword, ignoreCase = true)
            }
            _uiState.update { state ->
                when (state) {
                    is UiState.Success -> {
                        UiState.Success(
                            state.data.copy(
                                friends = result
                            )
                        )
                    }

                    else -> state
                }
            }
        }
    }

    fun addGroupMember(
        member: User
    ) {
        viewModelScope.launch {
            _uiState.update { state ->
                when (state) {
                    is UiState.Success -> {
                        val groupMember = state.data.groupMember + member
                        UiState.Success(
                            state.data.copy(
                                groupMember = groupMember
                            )
                        )
                    }

                    else -> state
                }
            }
        }
    }

    fun removeGroupMember(
        member: User
    ) {
        viewModelScope.launch {
            _uiState.update { state ->
                when (state) {
                    is UiState.Success -> {
                        val groupMember = state.data.groupMember - member
                        UiState.Success(
                            state.data.copy(
                                groupMember = groupMember
                            )
                        )
                    }

                    else -> state
                }
            }
        }
    }

    fun createGroup(
        name: String,
        avatar: File? = null,
        otherUser: List<User>
    ) {
        viewModelScope.launch {
            _uiState.update { state ->
                when (state) {
                    is UiState.Success -> {
                        UiState.Success(
                            state.data.copy(
                                isGroupNameBlank = name.isBlank()
                            )
                        )
                    }

                    else -> state
                }
            }

            if(name.isBlank()) {
                return@launch
            }

            if(otherUser.size < 2) {
                _event.emit(CreateGroupEvent.NotEnough)
                return@launch
            }

            createGroupUseCase(
                name = name,
                avatar = avatar,
                otherUser = otherUser
            )
                .onSuccess { conversationId ->
                    _event.emit(
                        CreateGroupEvent.NavigateToConversationDetail(conversationId)
                    )
                }
                .onFailure {
                    _event.emit(CreateGroupEvent.ShowError)
                }
        }
    }
}