package com.example.chatapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.navigation.route.CameraRoute
import com.example.chatapplication.navigation.route.ChangePasswordRoute
import com.example.chatapplication.navigation.route.ContactsRoute
import com.example.chatapplication.navigation.route.ConversationDetailRoute
import com.example.chatapplication.navigation.route.ConversationOptionRoute
import com.example.chatapplication.navigation.route.ConversationRoute
import com.example.chatapplication.navigation.route.CreateGroupRoute
import com.example.chatapplication.navigation.route.ForgotPasswordRoute
import com.example.chatapplication.navigation.route.ForwardRoute
import com.example.chatapplication.navigation.route.FriendRequestRoute
import com.example.chatapplication.navigation.route.FriendSuggestionRoute
import com.example.chatapplication.navigation.route.LanguageRoute
import com.example.chatapplication.navigation.route.LoginRoute
import com.example.chatapplication.navigation.route.ProfileRoute
import com.example.chatapplication.navigation.route.RegisterRoute
import com.example.chatapplication.navigation.route.SearchRoute
import com.example.chatapplication.navigation.route.SentFriendRequestRoute
import com.example.chatapplication.navigation.route.SettingsRoute
import com.example.chatapplication.navigation.route.ViewMemberRoute
import com.example.core_model.enum.SettingOptionAction
import com.example.core_ui.menu.AppBottomBarAction

@Composable
fun AppNavHost(
    startDestination: String,
    darkTheme: Boolean
) {
    val navController = rememberNavController()

    var selectedAction: AppBottomBarAction by remember {
        mutableStateOf(AppBottomBarAction.CHAT)
    }

    val onSearchClick: () -> Unit = {
        navController.navigate(AppRoute.SEARCH)
    }

    val onScannerClick: () -> Unit = {
        navController.navigate(AppRoute)
    }

    val onNavigationToConversationDetail: (String) -> Unit = { conversationId ->
        navController.navigate("${AppRoute.CONVERSATION_DETAIL}/$conversationId")
    }

    val onViewAllClick: () -> Unit = {
        navController.navigate(AppRoute.FRIEND_REQUEST)
    }

    val onViewMoreClick: () -> Unit = {
        navController.navigate(AppRoute.FRIEND_SUGGESTION)
    }

    val onCreateNewAccountClick: () -> Unit = {
        navController.navigate(AppRoute.REGISTER)
    }

    val onHasAccountClick: () -> Unit = {
        navController.navigate(AppRoute.LOGIN)
    }

    val onBack: () -> Unit = {
        navController.navigateUp()
    }

    val onBottomActionClick: (AppBottomBarAction) -> Unit = { action ->
        when(action) {
            AppBottomBarAction.CHAT -> {
                navController.navigate(AppRoute.CONVERSATION)
            }

            AppBottomBarAction.CONTACTS -> {
                navController.navigate(AppRoute.CONTACTS)
            }

            AppBottomBarAction.EXPLORE -> {

            }

            AppBottomBarAction.PERSON -> {
                navController.navigate(AppRoute.PERSON)
            }
        }
    }

    val onSettingOptionClick: (SettingOptionAction) -> Unit = { action ->
        when(action) {
            SettingOptionAction.QR_CODE -> {
                navController.navigate(AppRoute.PROFILE)
            }

            SettingOptionAction.PROFILE -> {
                navController.navigate(AppRoute.PROFILE)
            }

            SettingOptionAction.PASSWORD -> {
                navController.navigate(AppRoute.CHANGE_PASSWORD)
            }

            SettingOptionAction.FONT_SIZE -> {
                navController.navigate(AppRoute.CHANGE_PASSWORD)
            }

            SettingOptionAction.LANGUAGE -> {
                navController.navigate(AppRoute.LANGUAGE)
            }

            SettingOptionAction.CACHE_MEMORY -> {
                navController.navigate(AppRoute.CHANGE_PASSWORD)
            }

            SettingOptionAction.HELP_CENTER -> {
                navController.navigate(AppRoute.CHANGE_PASSWORD)
            }

            SettingOptionAction.LOGOUT -> {
                navController.navigate(AppRoute.LOGIN)
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppRoute.CONVERSATION) {
            ConversationRoute(
                onSearchClick = onSearchClick,
                onNavigationToConversationDetail = onNavigationToConversationDetail,
                onBottomActionClick = onBottomActionClick,
                onScannerClick = onScannerClick
            )
        }

        composable(route = AppRoute.CONVERSATION_DETAIL_WITH_ARG) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                    ?: return@composable
            ConversationDetailRoute(
                conversationId = conversationId,
                darkTheme = darkTheme,
                onOption = {
                    navController.navigate("${AppRoute.CONVERSATION_OPTION}/$conversationId")
                },
                onOpenCamera = {
                    navController.navigate(AppRoute.CAMERA)
                },
                onBack = onBack,
                onForward = { conversationId, messageId ->
                    navController.navigate("${AppRoute.FORWARD}/$conversationId/$messageId")
                }
            )
        }

        composable(route = AppRoute.CONVERSATION_OPTION_WITH_ARG) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                    ?: return@composable
            ConversationOptionRoute(
                conversationId = conversationId,
                onBack = onBack,
                onCreateGroup = { userId ->
                    navController.navigate("${AppRoute.CREATE_GROUP}/${userId}")
                },
                onViewGroupMember = { conversationId ->
                    navController.navigate("${AppRoute.VIEW_MEMBER}/${conversationId}")
                }
            )
        }

        composable(route = AppRoute.CREATE_GROUP_WITH_ARG) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getString("userId")
                ?: return@composable
            CreateGroupRoute(
                friendId = friendId,
                onNavigationToConversationDetail = { conversationId ->
                    onNavigationToConversationDetail(conversationId)
                },
                onBack = onBack
            )
        }

        composable(route = AppRoute.SEARCH) {
            SearchRoute(
                selectedAction = selectedAction,
                onBack = onBack,
                onNavigationToConversationDetail = onNavigationToConversationDetail,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.CONTACTS) {
            ContactsRoute(
                onFriendRequest = {
                    navController.navigate(AppRoute.FRIEND_REQUEST)
                },
                onFriendSent = {
                    navController.navigate(AppRoute.FRIEND_SENT)
                },
                onFriendSuggestion = {
                    navController.navigate(AppRoute.FRIEND_SUGGESTION)
                },
                onContact = { conversationId ->
                    navController.navigate("${AppRoute.CONVERSATION_DETAIL}/$conversationId")
                },
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.FRIEND_REQUEST) {
            FriendRequestRoute(
                onBack = onBack,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.FRIEND_SENT) {
            SentFriendRequestRoute(
                onBack = onBack,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.FRIEND_SUGGESTION) {
            FriendSuggestionRoute(
                onBack = onBack,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.PERSON) {
            SettingsRoute(
                onSearchClick = onSearchClick,
                onSettingOptionClick = onSettingOptionClick,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.PROFILE) {
            ProfileRoute(
                onBack = onBack
            )
        }

        composable(route = AppRoute.CHANGE_PASSWORD) {
            ChangePasswordRoute (
                onBack = onBack,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.LANGUAGE) {
            LanguageRoute(
                onBack = onBack,
                onBottomActionClick = onBottomActionClick
            )
        }

        composable(route = AppRoute.LOGIN) { backStackEntry ->
            val showDialog = backStackEntry.arguments
                ?.getString("showDialog")?.toBoolean() ?: false
            LoginRoute(
                showDialog = showDialog,
                onNavigateToHome = {
                    navController.navigate(AppRoute.CONVERSATION)
                },
                onCreateNewAccountClick = onCreateNewAccountClick,
                onForgotPasswordClick = {
                    navController.navigate(AppRoute.FORGOT_PASSWORD)
                }
            )
        }

        composable(route = AppRoute.REGISTER) {
            RegisterRoute(
                onNavigateToLogin = {
                    navController.navigate("login?showDialog=true")
                },
                onHasAccountClick = onHasAccountClick
            )
        }

        composable(route = AppRoute.FORGOT_PASSWORD) {
            ForgotPasswordRoute(
                onNavigateToLogin = {
                    navController.navigate("login?showDialog=false")
                }
            )
        }

        composable(route = AppRoute.VIEW_MEMBER_WITH_ARG) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                ?: return@composable
            ViewMemberRoute(
                conversationId = conversationId,
                onBack = onBack
            )
        }

        composable(route = AppRoute.CAMERA) {
            CameraRoute(
                onImageCaptured = { uri ->
                    // xử lý ảnh
                },
                onBack = onBack
            )
        }

        composable(route = AppRoute.FORWARD_WITH_ARG) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId")
                ?: return@composable
            val messageId = backStackEntry.arguments?.getString("messageId")
                ?: return@composable
            ForwardRoute(
                conversationId = conversationId,
                messageId = messageId,
                onBack = onBack
            )
        }
    }
}