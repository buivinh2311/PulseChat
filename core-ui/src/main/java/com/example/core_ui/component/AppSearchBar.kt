package com.example.core_ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R

@Composable
fun AppSearchBar(
    onSearch: (String) -> Unit
) {
    var keyword by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(AppDimens.Space.Md)
            .clip(RoundedCornerShape(AppDimens.Radius.Sm))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(AppDimens.Space.Md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = AppIcons.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(AppDimens.Space.Sm))

        BasicTextField(
            value = keyword,
            onValueChange = {
                keyword = it
                onSearch(it)
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Start
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .weight(1f)
                .height(AppDimens.Space.Xl)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (keyword.isEmpty()) {
                        Text(
                            text = stringResource(R.string.hint_query),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondary.copy(
                                alpha = 0.7f
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}