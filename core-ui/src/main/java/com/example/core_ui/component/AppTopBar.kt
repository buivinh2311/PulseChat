package com.example.core_ui.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color? = null,
    subTitle: String? = null,
    onBack: (() -> Unit)? = null,
    onScan: (() -> Unit)? = null
) {
    val gradientColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
    val strokeWidth = AppDimens.Divider.Normal

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppDimens.Layout.TopBarHeight)
            .background(color = MaterialTheme.colorScheme.surface)
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
            .padding(horizontal = AppDimens.Space.Md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        onBack?.let {
            AppIconButton(
                icon = AppIcons.Back,
                onClick = onBack
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Md))
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = if (onBack == null) MaterialTheme.typography.headlineLarge
                else MaterialTheme.typography.titleMedium,
                color = titleColor ?: MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(AppDimens.Space.Xs))

            subTitle?.let {
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        onScan?.let {
            AppIconButton(
                icon = AppIcons.Scanner,
                color = MaterialTheme.colorScheme.onBackground,
                iconSize = AppDimens.Icon.Sm,
                onClick = onScan
            )
        }
    }
}