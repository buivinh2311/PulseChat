package com.example.feature_conversation.presentation.viewmember

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.R

@Composable
fun ViewGroupMemberItem(
    modifier: Modifier = Modifier,
//    friendWithStatus: Us
) {
//    Surface(
//        modifier = modifier
//            .height(AppDimens.Layout.ChatItemHeight)
//            .padding(horizontal = AppDimens.Space.Md)
//            .shadow(
//                elevation = AppDimens.Space.Es,
//                shape = RoundedCornerShape(AppDimens.Space.Lg)
//            ),
//        onClick = {
//
//        },
//        shape = RoundedCornerShape(AppDimens.Space.Lg),
//        color = MaterialTheme.colorScheme.surfaceContainerLowest
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(
//                    horizontal = AppDimens.Space.Md
//                ),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box {
//                AsyncImage(
//                    model = friendWithStatus.friend.avatarUrl,
//                    contentDescription = null,
//                    placeholder = painterResource(R.drawable.account),
//                    error = painterResource(R.drawable.account),
//                    modifier = Modifier
//                        .size(AppDimens.ImageSize.Sm)
//                        .clip(shape = CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//
//                if (friendWithStatus.isOnline) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .size(AppDimens.Icon.Xs)
//                            .clip(shape = CircleShape)
//                            .background(color = Color(0xFF31A24C))
//                            .border(
//                                width = AppDimens.Border.Thick - AppDimens.Border.Normal,
//                                color = MaterialTheme.colorScheme.background,
//                                shape = CircleShape
//                            )
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.width(AppDimens.Space.Md))
//
//            Text(
//                text = friendWithStatus.friend.fullName,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.weight(1f)
//            )
//        }
//    }
}