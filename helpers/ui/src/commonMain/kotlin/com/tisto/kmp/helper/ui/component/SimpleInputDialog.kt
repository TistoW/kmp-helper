package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.TextAppearance
import kotlinx.coroutines.delay


@Composable
fun SimpleInputDialog(
    title: String,
    subtitle: String = "",
    hint: String = "",
    defaultValue: String = "",
    actionText: String = "Simpan",
    reqFocusInput: Boolean = false,
    onDismissRequest: () -> Unit,
    onAction: (String) -> Unit,
) {
//    var input by remember { mutableStateOf(defaultValue) }
    val focusRequester = remember { FocusRequester() }

    val textState = remember { mutableStateOf(TextFieldValue(text = defaultValue)) }

    LaunchedEffect(Unit) {
        textState.value = TextFieldValue(
            text = defaultValue,
            selection = TextRange(defaultValue.length) // cursor di akhir
        )
    }

    LaunchedEffect(Unit) {
        if (reqFocusInput) {
            delay(100) // Give Compose a moment before requesting focus
            focusRequester.requestFocus()
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                )

                // Subtitle
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .padding(horizontal = 16.dp),
                    value = textState.value,
                    onValueChange = { textState.value = it },
                    textStyle = TextAppearance.body3()
                )

                // Text Input
//                TextFieldOutlined(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusRequester)
//                        .padding(horizontal = 16.dp),
//                    value = textState.value,
//                    hint = hint,
//                    onValueChange = { textState.value = it },
//                )


//                OutlinedTextField(
//                    value = input,
//                    onValueChange = { input = it },
//                    placeholder = { Text(text = hint) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .focusRequester(focusRequester),
//                    maxLines = 2
//                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            val value = textState.value.text
                            if (value.isNotEmpty()) {
                                onAction(value)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = actionText,
                        style = TextAppearance.body3Bold(),
                        color = Colors.Black
                    )
                }
                HorizontalDivider(thickness = 0.5.dp)
                // Close Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clickable {
                            onDismissRequest()
                        }
                        .background(Colors.Gray5),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun SimpleInputDialogPreview() {
    SimpleInputDialog(
        title = "Simple Input",
        subtitle = "Subtitle simple inputing data",
        hint = "Input text",
        defaultValue = "Default Value",
        onDismissRequest = {},
        onAction = {}
    )
}