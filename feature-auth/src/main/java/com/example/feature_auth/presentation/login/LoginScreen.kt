package com.example.feature_auth.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_model.result.LoginResult
import com.example.core_model.result.ResendResult
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.LoadingScreen
import com.example.feature_auth.presentation.login.LoginEvent
import com.example.feature_auth.presentation.login.LoginViewModel
import com.example.share_presentation.AppTopBanner
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    showDialog: Boolean = false,
    onCreateNewAccountClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isShowPassword by remember {
        mutableStateOf(false)
    }

    var showBanner by remember {
        mutableStateOf(false)
    }

    var showVerifyEmail by remember {
        mutableStateOf(showDialog)
    }

    var remainingSeconds by remember {
        mutableIntStateOf(60)
    }

    val loginViewModel: LoginViewModel = hiltViewModel()
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when (event) {
                LoginEvent.NavigateToHome -> onNavigateToHome()
                LoginEvent.ShowBanner -> {
                    showBanner = true
                    delay(3000)
                    showBanner = false
                }
            }
        }
    }

    LaunchedEffect(remainingSeconds) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
                    .fillMaxSize()
                    .padding(
                        vertical = AppDimens.Space.Xl,
                        horizontal = AppDimens.Space.Lg
                    )
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.account),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.Space.Xl))

                    Text(
                        text = stringResource(R.string.title_login),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.hint_email),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

                    AnimatedVisibility(
                        visible = uiState.isEmailBlank
                    ) {
                        Text(
                            text = stringResource(R.string.text_email_blank),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(
                                start = AppDimens.Space.Sm
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.hint_password),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        visualTransformation = if (isShowPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    isShowPassword = !isShowPassword
                                }
                            ) {
                                Icon(
                                    painter = if (isShowPassword) {
                                        AppIcons.HidePassword
                                    } else {
                                        AppIcons.ShowPassword
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(AppDimens.Icon.Sm)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

                    AnimatedVisibility(
                        visible = uiState.isPasswordBlank
                    ) {
                        Text(
                            text = stringResource(R.string.text_password_blank),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(
                                start = AppDimens.Space.Sm
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                    Button(
                        onClick = {
                            loginViewModel.login(email, password)
                        },
                        shape = RoundedCornerShape(AppDimens.Radius.Sm),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimens.Layout.ButtonHeight)
                    ) {
                        Text(
                            text = stringResource(R.string.label_login),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

                    Button(
                        onClick = onCreateNewAccountClick,
                        shape = RoundedCornerShape(AppDimens.Radius.Sm),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF31A24C)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppDimens.Layout.ButtonHeight)
                    ) {
                        Text(
                            text = stringResource(R.string.label_create_new_register),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(AppDimens.Space.Xs))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = {
                                onForgotPasswordClick()
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.label_forget),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (showBanner) {
                if (uiState.loginResult != null) {
                    AppTopBanner(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .zIndex(1f),
                        title = when (uiState.loginResult) {
                            LoginResult.Failure.EmailNotVerified -> {
                                stringResource(R.string.text_email_not_verified)
                            }

                            LoginResult.Failure.InvalidCredentials -> {
                                stringResource(R.string.text_invalid_credential)
                            }

                            LoginResult.Failure.UserDisabled -> {
                                stringResource(R.string.text_user_disabled)
                            }

                            LoginResult.Failure.TooManyRequests -> {
                                stringResource(R.string.text_too_many_requests)
                            }

                            LoginResult.Failure.Network -> {
                                stringResource(R.string.text_network_error)
                            }

                            LoginResult.Failure.Unknown -> {
                                stringResource(R.string.text_unknown_error)
                            }

                            else -> {
                                " "
                            }
                        }
                    )
                }
            }

            if (showVerifyEmail) {
                Dialog(
                    onDismissRequest = {
                        showVerifyEmail = false
                    }
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimens.Radius.Xl)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppDimens.Space.Lg),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.title_verify_email),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(AppDimens.Space.Lg))

                            Text(
                                text = stringResource(R.string.text_verity_email),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.height(AppDimens.Space.Xl))

                            if (uiState.resendResult != null &&
                                uiState.resendResult != ResendResult.Success
                            ) {
                                Text(
                                    text = stringResource(R.string.text_unknown_error),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(AppDimens.Space.Lg))
                            } else if (remainingSeconds > 0) {
                                Text(
                                    text = stringResource(
                                        R.string.text_sent_verification_email,
                                        remainingSeconds
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(AppDimens.Space.Lg))
                            }

                            HorizontalDivider(
                                thickness = AppDimens.Divider.Normal,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            Button(
                                onClick = {
                                    loginViewModel.resendEmailVerification()
                                    remainingSeconds = 60
                                },
                                enabled = remainingSeconds <= 0,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(AppDimens.Radius.Sm),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Transparent
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.label_resend),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = AppDimens.Space.Xs)
                                )
                            }

                            HorizontalDivider(
                                thickness = AppDimens.Divider.Normal,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            Button(
                                onClick = {
                                    showVerifyEmail = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = AppDimens.Space.Xs),
                                shape = RoundedCornerShape(AppDimens.Radius.Sm),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.Transparent
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.label_close),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = AppDimens.Space.Xs)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}