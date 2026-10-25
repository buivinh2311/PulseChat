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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.example.core_resource.AppDimens

@Composable
fun ButtonToggleItem(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    icon: Painter,
    iconColor: Color,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    var isChecked by remember {
        mutableStateOf(isChecked)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                isChecked = !isChecked
                onToggle(isChecked)
            }
            .padding(
                top = AppDimens.Space.Sm,
                bottom = AppDimens.Space.Sm,
                start = AppDimens.Space.Lg,
                end = AppDimens.Space.Lg
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(AppDimens.ImageSize.Sm)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer),
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
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        AppSwitch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = !isChecked
                onToggle(isChecked)
            }
        )
    }
}