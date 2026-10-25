package com.example.core_ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.core_resource.AppDimens
import com.example.core_resource.R

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    field: String,
    value: String,
    hint: String,
    enable: Boolean = true,
    error: String? = null,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            AppDimens.Space.Xs + AppDimens.Space.Xxs
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
        ) {
            Text(
                text = field,
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )

            error?.let {
                Text(
                    text = stringResource(R.string.title_not_blank),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            enabled = enable,
            textStyle = typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSecondary
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimens.Layout.TextFieldHeight)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(AppDimens.Radius.Sm)
                )
                .border(
                    width = AppDimens.Border.Normal,
                    color = if(value.isBlank()) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(AppDimens.Radius.Sm)
                )
                .padding(horizontal = AppDimens.Space.Md)
                .wrapContentHeight(Alignment.CenterVertically),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondary.copy(
                                alpha = 0.7f
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        error?.let {
            AnimatedVisibility(
                visible = value.isBlank()
            ) {
                Spacer(modifier = Modifier.height(AppDimens.Space.Xs + AppDimens.Space.Xxs))
                Text(
                    text = error,
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}