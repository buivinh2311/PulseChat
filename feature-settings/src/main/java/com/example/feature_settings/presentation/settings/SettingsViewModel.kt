package com.example.feature_settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_domain.usecase.ObserverUserUseCase
import com.example.core_domain.usecase.UploadAvatarUseCase
import com.example.core_model.enum.DarkModeState
import com.example.core_ui.state.UiState
import com.example.feature_settings.usecase.GetThemeModeUseCase
import com.example.feature_settings.usecase.LogoutUseCase
import com.example.feature_settings.usecase.SetThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val observerUserUseCase: ObserverUserUseCase,
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<SettingsUiState>>(
        UiState.Loading
    )
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SettingsEvent>()
    val event = _event.asSharedFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            combine(
                observerUserUseCase().filterNotNull(),
                getThemeModeUseCase()
            ) { currentUser, darkModeState ->
                SettingsUiState(
                    currentUser = currentUser,
                    darkModeState = darkModeState
                )
            }
                .catch {
                    _uiState.value = UiState.Error
                }
                .collect { state ->
                    _uiState.value = UiState.Success(state)
                }
        }
    }

    fun uploadAvatar(file: File) {
        viewModelScope.launch {
            uploadAvatarUseCase(file)
                .onFailure {
                    _event.emit(SettingsEvent.ShowError)
                }
        }
    }

    fun toggleThemeState(state: DarkModeState) {
        viewModelScope.launch {
            setThemeModeUseCase(state)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}