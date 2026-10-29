package com.example.core_ui.component

import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.core_resource.AppDimens

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    containerColor: Color,
    label: String,
    labelColor: Color,
    style: TextStyle,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(AppDimens.Radius.Sm),
        color = containerColor,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = style,
                color = labelColor,
                modifier = Modifier.padding(horizontal = AppDimens.Space.Md)
            )
        }
    }
}