package com.example.feature_contacts.friendrequest

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.core_resource.AppDimens
import com.example.core_resource.R
import com.example.core_ui.component.AppBottomBar
import com.example.core_ui.component.AppTopBar
import com.example.core_ui.menu.AppBottomBarAction

@Composable
fun FriendRequestScreen(
    onBack: () -> Unit,
    onBottomActionClick: (AppBottomBarAction) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.title_friend_request),
                onBack = onBack
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedAction = AppBottomBarAction.CONTACTS,
                onBottomActionClick = onBottomActionClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            contentPadding = PaddingValues(
                vertical = AppDimens.Space.Sm
            )
        ) {
//            items(
//                count = 10
//            ) {
//                FriendRequest(
//
//                    onClick = { }
//                )
//            }
        }
    }
}