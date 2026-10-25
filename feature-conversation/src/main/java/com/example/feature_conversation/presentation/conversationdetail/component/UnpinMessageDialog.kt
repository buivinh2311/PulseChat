package com.example.feature_conversation.presentation.conversationdetail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.core_model.Message
import com.example.core_resource.AppDimens
import com.example.core_resource.R

@Composable
fun UnpinMessageDialog(
    message: Message,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(
                    R.string.text_unpin_message,
                    messagePreviewText(message)
                ),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.label_unpin),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.label_cancel),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    )
}