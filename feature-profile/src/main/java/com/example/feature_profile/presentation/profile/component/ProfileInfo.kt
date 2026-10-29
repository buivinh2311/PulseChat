package com.example.feature_profile.presentation.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import com.example.core_model.enum.Gender
import com.example.core_resource.AppDimens
import com.example.core_resource.AppIcons
import com.example.core_resource.R
import com.example.core_ui.component.AppButton
import com.example.core_ui.component.AppTextField
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ProfileInfo(
    modifier: Modifier = Modifier,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    selectedGender: Gender? = Gender.MALE,
    onGenderSelected: (Gender) -> Unit,
    selectedDate: Timestamp?,
    onDateSelected: (Timestamp) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit
) {
    val genders = Gender.entries
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                return year <= currentYear
            }

            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.Space.Md)
            .shadow(
                elevation = AppDimens.Space.Es,
                shape = RoundedCornerShape(AppDimens.Space.Lg)
            ),
        shape = RoundedCornerShape(AppDimens.Space.Lg),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.Space.Lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppDimens.Space.Lg)
        ) {
            Text(
                text = stringResource(R.string.title_basic_info),
                style = typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )

            AppTextField(
                field = stringResource(R.string.title_full_name),
                value = fullName,
                hint = stringResource(R.string.hint_full_name),
                error = stringResource(R.string.text_full_name_blank),
                onValueChange = { fullName ->
                    onFullNameChange(fullName)
                }
            )

            AppTextField(
                field = stringResource(R.string.title_email),
                value = email,
                hint = stringResource(R.string.hint_email),
                error = stringResource(R.string.text_email_can_not_blank),
                enable = false,
                onValueChange = {}
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    AppDimens.Space.Xs + AppDimens.Space.Xxs
                )
            ) {
                Text(
                    text = stringResource(R.string.title_gender),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.Layout.TextFieldHeight)
                        .clip(RoundedCornerShape(AppDimens.Radius.Sm))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(AppDimens.Space.Xs),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs)
                ) {
                    genders.forEach { gender ->
                        val selected = selectedGender == gender
                        AppButton(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            containerColor = if (selected)
                                MaterialTheme.colorScheme.surfaceContainerLowest
                            else
                                MaterialTheme.colorScheme.secondary,
                            label = gender.displayName(),
                            labelColor = if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSecondary,
                            style = typography.titleMedium,
                            onClick = {
                                onGenderSelected(gender)
                            }
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    AppDimens.Space.Xs + AppDimens.Space.Xxs
                )
            ) {
                Text(
                    text = stringResource(R.string.title_date_of_birth),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.Layout.TextFieldHeight)
                        .clip(RoundedCornerShape(AppDimens.Radius.Sm))
                        .clickable {
                            showDatePicker = true
                        }
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = AppDimens.Space.Md),
                    horizontalArrangement = Arrangement.spacedBy(AppDimens.Space.Xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedDate?.toDisplayString() ?: "dd/MM/yyyy",
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        painter = AppIcons.Calendar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(AppDimens.Icon.Sm)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(
                    AppDimens.Space.Xs + AppDimens.Space.Xxs
                )
            ) {
                Text(
                    text = stringResource(R.string.title_bio),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )

                BasicTextField(
                    value = bio,
                    onValueChange = { bio ->
                        onBioChange(bio)
                    },
                    minLines = 6,
                    maxLines = 6,
                    textStyle = typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(AppDimens.Radius.Sm)
                        )
                        .border(
                            width = AppDimens.Border.Normal,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(AppDimens.Radius.Sm)
                        )
                        .padding(AppDimens.Space.Md),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (bio.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.hint_bio),
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
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            onDateSelected(Timestamp(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.label_confirm),
                        style = typography.titleMedium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.label_cancel),
                        style = typography.titleMedium
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
fun Gender.displayName(): String {
    return when (this) {
        Gender.MALE -> stringResource(R.string.label_male)
        Gender.FEMALE -> stringResource(R.string.label_female)
        Gender.OTHER -> stringResource(R.string.label_other)
    }
}

fun Timestamp.toDisplayString(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(toDate())
}