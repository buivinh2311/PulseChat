package com.example.feature_profile.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconTextButton
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.component.showToast
import com.example.core_ui.menu.AppBottomBarAction
import com.example.core_ui.state.UiState
import com.example.feature_profile.presentation.profile.component.ProfileAvatar
import com.example.feature_profile.presentation.profile.component.ProfileInfo

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        profileViewModel.loadCurrentUser()
        profileViewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.ShowError -> {
                    showToast(
                        context,
                        message = context.getString(R.string.text_unknown_error)
                    )
                }

                is ProfileEvent.UpdateSuccessful -> {
                    showToast(
                        context,
                        message = context.getString(R.string.text_update_success)
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                AppTopBar(
                    title = stringResource(R.string.title_edit_profile),
                    onBack = onBack
                )
            }
        ) { innerPadding ->
            when (val state = uiState) {
                UiState.Loading -> {
                    LoadingScreen()
                }

                UiState.Error -> {
                    ErrorScreen(title = "ERROR")
                }

                UiState.Empty -> {

                }

                is UiState.Success -> {
                    val data = state.data
                    val user = data.currentUser
                    var fullName by remember {
                        mutableStateOf(user.fullName)
                    }

                    var bio by remember {
                        mutableStateOf(user.bio.orEmpty())
                    }

                    var selectedDate by remember {
                        mutableStateOf(user.dateOfBirth)
                    }

                    var selectedGender by remember {
                        mutableStateOf(user.gender)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .imePadding()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = AppDimens.Space.Lg,
                                    end = AppDimens.Space.Md,
                                    top = AppDimens.Space.Xs,
                                    bottom = AppDimens.Space.Xs
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

                            Text(
                                text = stringResource(R.string.editing),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                            AppIconTextButton(
                                modifier = Modifier.padding(vertical = AppDimens.Space.Xxs),
                                containerColor = MaterialTheme.colorScheme.primary,
                                icon = AppIcons.Check,
                                iconColor = MaterialTheme.colorScheme.onPrimary,
                                iconSize = AppDimens.Icon.Sm,
                                label = stringResource(R.string.label_save),
                                style = MaterialTheme.typography.titleMedium,
                                labelColor = MaterialTheme.colorScheme.onPrimary,
                                onClick = {
                                    profileViewModel.updateProfile(
                                        user = user,
                                        fullName = fullName,
                                        bio = bio,
                                        dateOfBirth = selectedDate,
                                        gender = selectedGender
                                    )
                                }
                            )
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(
                                bottom = AppDimens.Space.Xl
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                ProfileAvatar(
                                    context = context,
                                    avatar = user.avatarUrl,
                                    coverPhoto = user.coverPhotoUrl,
                                    uploadAvatar = { file ->
                                        profileViewModel.uploadAvatar(file)
                                    },
                                    uploadCoverPhoto = { file ->
                                        profileViewModel.uploadCoverPhoto(file)
                                    },
                                    onNavigateToQRCode = {  }
                                )
                                Spacer(
                                    modifier = Modifier.height(
                                        AppDimens.Space.El + AppDimens.Space.Xl
                                    )
                                )

                                ProfileInfo(
                                    fullName = fullName,
                                    onFullNameChange = { fullName = it },
                                    email = user.email,
                                    selectedGender = selectedGender,
                                    onGenderSelected = { selectedGender = it },
                                    selectedDate = selectedDate,
                                    onDateSelected = { selectedDate = it },
                                    bio = bio,
                                    onBioChange = { bio = it }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}