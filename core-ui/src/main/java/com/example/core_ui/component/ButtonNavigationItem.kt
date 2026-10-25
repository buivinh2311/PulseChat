package com.example.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons

@Composable
fun ButtonNavigateItem(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    textColor: Color? = null,
    icon: Painter,
    iconColor: Color,
    containerColor: Color? = null,
    onNavigate: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if(onNavigate != null) {
                    onNavigate()
                } else if(onClick != null){
                    onClick()
                }
            }
            .padding(
                top = AppDimens.Space.Sm,
                bottom = AppDimens.Space.Sm,
                start = AppDimens.Space.Lg,
                end = AppDimens.Space.Sm
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.ImageSize.Sm)
                .clip(CircleShape)
                .background(
                    containerColor
                        ?: MaterialTheme.colorScheme.secondaryContainer
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(AppDimens.Icon.Sm)
            )
        }
        Spacer(modifier = Modifier.width(AppDimens.Space.Md))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Xxs)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor ?: MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = textColor?.copy(alpha = 0.8f) ?: MaterialTheme.colorScheme.outline
            )
        }

        onNavigate?.let {
            Icon(
                painter = AppIcons.Next,
                contentDescription = null,
                modifier = Modifier.size(AppDimens.Icon.Sm)
            )
        }
    }
}