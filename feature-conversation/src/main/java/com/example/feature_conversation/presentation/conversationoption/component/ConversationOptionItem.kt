package com.example.feature_conversation.presentation.conversationoption.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import com.example.core_resource.AppDimens

@Composable
fun ConversationOptionItem(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter,
    iconColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.Space.Sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(AppDimens.ImageSize.Xs)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(AppDimens.Icon.Xs)
                )
            }
            Spacer(modifier = Modifier.height(AppDimens.Space.Sm))

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}