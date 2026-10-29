package com.example.feature_contacts.presentation.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.GetCurrentUserIdUseCase
import com.example.core_domain.usecase.GetFriendsUseCase
import com.example.core_ui.state.UiState
import com.example.core_domain.usecase.GetGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
): ViewModel() {
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

            val friends = getFriendsUseCase()
                .getOrElse {
                    _uiState.value = UiState.Error
                    return@launch
                }

            getGroupsUseCase()
                .onFailure {
                    _uiState.value = UiState.Error
                    return@launch
                }
                .onSuccess { groups ->
                    _uiState.value = UiState.Success(
                        ContactsUiState(
                            currentUserId = currentUserId,
                            friends = friends,
                            groups = groups
                        )
                    )
                }
        }
    }
}