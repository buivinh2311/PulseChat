package com.example.feature_conversation.presentation.conversationoption.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.example.core_resource.R
import com.example.core_ui.component.AppTextField
import kotlinx.coroutines.delay

@Composable
fun ChangeGroupNameDialog (
    oldGroupName: String,
    onDismiss: () -> Unit,
    onChange: (String) -> Unit
) {
    var groupName by remember {
        mutableStateOf(
            TextFieldValue(
                text = oldGroupName,
                selection = TextRange(oldGroupName.length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    val isNameValid = groupName.text.isNotBlank()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.title_change_group_name),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = {
                    groupName = it
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.hint_group_name),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f)
                    )
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val name = groupName.text.trim()
                    if (name.isNotEmpty()) {
                        onChange(name)
                    }
                },
                enabled = isNameValid
            ) {
                Text(
                    text = stringResource(R.string.action_confirm),
                    style = MaterialTheme.typography.titleMedium,
                    color = if(isNameValid) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}