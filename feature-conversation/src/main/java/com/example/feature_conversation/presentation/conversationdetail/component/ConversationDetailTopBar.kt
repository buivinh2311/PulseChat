package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppIconButton

@Composable
fun ConversationDetailTopBar(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String,
    onBack: () -> Unit,
    onOption: () -> Unit
) {
    val gradientColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
    val strokeWidth = AppDimens.Divider.Normal

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.surface)
            .drawBehind(
                onDraw = {
                    drawLine(
                        color = gradientColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth.toPx()
                    )
                }
            )
            .padding(
                vertical = AppDimens.Space.Xs,
                horizontal = AppDimens.Space.Md,
            )
            .height(AppDimens.Layout.TopBarHeight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppIconButton(
            icon = AppIcons.Back,
            color = MaterialTheme.colorScheme.onSurface,
            onClick = onBack
        )
        Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.account),
                error = painterResource(R.drawable.account),
                modifier = Modifier
                    .size(AppDimens.ImageSize.Xs)
                    .clip(shape = CircleShape),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .size(12.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xFF31A24C))
                    .border(
                        width = AppDimens.Border.Normal,
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            )
        }
        Spacer(modifier = Modifier.width(AppDimens.Space.Md))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis
            )

//            Text(
//                text = "last online",
//                style = MaterialTheme.typography.bodyMedium
//            )
        }
        Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

        AppIconButton(
            icon = AppIcons.Call,
            color = MaterialTheme.colorScheme.onSecondary,
            onClick = { }
        )
        Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

        AppIconButton(
            icon = AppIcons.VideoCall,
            color = MaterialTheme.colorScheme.onSecondary,
            onClick = { }
        )
        Spacer(modifier = Modifier.width(AppDimens.Space.Lg))

        AppIconButton(
            icon = AppIcons.Option,
            color = MaterialTheme.colorScheme.onSecondary,
            onClick = onOption
        )
    }
}