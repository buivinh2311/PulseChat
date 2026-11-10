package com.example.feature_contacts.presentation.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppBottomBar
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.component.ErrorScreen
import com.example.core_ui.component.LoadingScreen
import com.example.core_ui.menu.AppBottomBarAction
import com.example.core_ui.state.UiState
import com.example.core_utils.ConversationIdUtil
import com.example.feature_contacts.domain.model.ContactsCategory
import com.example.feature_contacts.presentation.contacts.component.ContactsCategory
import com.example.feature_contacts.presentation.contacts.component.ContactsFriendItem
import com.example.feature_contacts.presentation.contacts.component.ContactsFriendRequest
import com.example.feature_contacts.presentation.contacts.component.ContactsFriendSent
import com.example.feature_contacts.presentation.contacts.component.ContactsFriendSuggest
import com.example.feature_contacts.presentation.contacts.component.ContactsGroupItem
import com.example.feature_contacts.presentation.contacts.component.ContactsSearchBar

@Composable
fun ContactsScreen(
    onFriendRequest: () -> Unit,
    onFriendSent: () -> Unit,
    onFriendSuggestion: () -> Unit,
    onContact: (String) -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    val contactsViewModel: ContactsViewModel = hiltViewModel()
    val uiState by contactsViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        contactsViewModel.loadData()
    }

    var contactsCategory by remember {
        mutableStateOf(ContactsCategory.FRIEND)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_everybody)
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedAction = AppBottomBarAction.CONTACTS,
                onBottomActionClick = onBottomActionClick
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            UiState.Loading -> {
                LoadingScreen()
            }

            UiState.Error -> {
                ErrorScreen(message = "ERROR")
            }

            UiState.Empty -> {

            }

            is UiState.Success -> {
                val data = state.data
                val friends = data.friends
                val groups = data.groups
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(
                        vertical = AppDimens.Space.Md
                    )
                ) {
                    item {
                        ContactsSearchBar()
                        Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                        ContactsFriendRequest(
                            onFriendRequest = onFriendRequest
                        )
                        Spacer(
                            modifier = Modifier.height(
                                AppDimens.Space.Xs + AppDimens.Space.Xxs
                            )
                        )

                        ContactsFriendSent(
                            onFriendSent = onFriendSent
                        )
                        Spacer(
                            modifier = Modifier.height(
                                AppDimens.Space.Xs + AppDimens.Space.Xxs
                            )
                        )

                        ContactsFriendSuggest(
                            onFriendSuggestion = onFriendSuggestion
                        )
                        Spacer(modifier = Modifier.height(AppDimens.Space.Md))

                        ContactsCategory(
                            contactsCategory = contactsCategory,
                            onChangeCategory = { category ->
                                contactsCategory = category
                            }
                        )
                    }

                    when (contactsCategory) {
                        ContactsCategory.FRIEND -> {
                            val groupedFriendWithStatuses = friends
                                .sortedBy { it.friend.fullName.trim().uppercase() }
                                .groupBy { friendWithStatus ->
                                    friendWithStatus.friend.fullName.trim()
                                        .firstOrNull()
                                        ?.uppercaseChar()
                                        ?.toString() ?: "#"
                                }
                                .toSortedMap()

                            groupedFriendWithStatuses.forEach { (letter, friendWithStatusesInGroup) ->
                                item(key = "header_$letter") {
                                    Spacer(modifier = Modifier.height(AppDimens.Space.Md))
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.padding(
                                            horizontal = AppDimens.Space.Md
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))
                                }

                                items(
                                    count = friendWithStatusesInGroup.size,
                                    key = { index -> friendWithStatusesInGroup[index].friend.id }
                                ) { index ->
                                    ContactsFriendItem(
                                        friendWithStatus = friendWithStatusesInGroup[index],
                                        onClick = { friendId ->
                                            val conversationId = ConversationIdUtil
                                                .buildDirectConversationId(
                                                    data.currentUserId,
                                                    friendId
                                                )
                                            onContact(conversationId)
                                        }
                                    )
                                    if(index != friendWithStatusesInGroup.size - 1) {
                                        Spacer(modifier = Modifier.height(
                                            AppDimens.Space.Xs + AppDimens.Space.Xxs)
                                        )
                                    }
                                }
                            }
                        }

                        ContactsCategory.GROUP -> {
                            val groupedGroups = groups
                                .sortedBy { it.name.trim().uppercase() }
                                .groupBy { friend ->
                                    friend.name.trim()
                                        .firstOrNull()
                                        ?.uppercaseChar()
                                        ?.toString() ?: "#"
                                }
                                .toSortedMap()

                            groupedGroups.forEach { (letter, groupsInGroup) ->
                                item(key = "header_$letter") {
                                    Spacer(modifier = Modifier.height(AppDimens.Space.Md))
                                    Text(
                                        text = letter,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.padding(
                                            horizontal = AppDimens.Space.Md
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(AppDimens.Space.Xxs))
                                }

                                items(
                                    count = groupsInGroup.size,
                                    key = { index -> groupsInGroup[index].id }
                                ) { index ->
                                    ContactsGroupItem(
                                        group = groupsInGroup[index],
                                        onClick = { conversationId ->
                                            onContact(conversationId)
                                        }
                                    )
                                    if(index != groupsInGroup.size - 1) {
                                        Spacer(modifier = Modifier.height(
                                            AppDimens.Space.Xs + AppDimens.Space.Xxs)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}