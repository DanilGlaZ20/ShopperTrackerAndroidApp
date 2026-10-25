package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.theme.Critical
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.theme.Success


@Composable
fun FindUser(
	onDismissRequest: () -> Unit,
	onConfirmation: () -> Unit,
	dialogTitle: String,
	dialogText: String,
	textContent: String,
	queryContent: String,
	onContentChanged: (String) -> Unit,
	successText: String? = null,
	errorText: String? = null

) {

	AlertDialog(
		title = {
			Text(text = dialogTitle)
		},
		text = {
			Column {
				Text(text = dialogText)
				Spacer(modifier = Modifier.height(8.dp))
				InputContent(
					modifier = Modifier.fillMaxWidth(),
					text = textContent,
					query = queryContent,
					onQueryChanged = onContentChanged,
					isPhone = true

				)
				if (!successText.isNullOrBlank()) {
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = successText,
						color = Success,
						fontSize = 14.sp,
						fontWeight = FontWeight.Medium
					)
				}

				if (!errorText.isNullOrBlank()) {
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = errorText,
						color = Critical,
						fontSize = 14.sp,
						fontWeight = FontWeight.Medium
					)
				}
			}

		},
		onDismissRequest = {
			onDismissRequest()
		},
		confirmButton = {
			TextButton(
				onClick = {
					onConfirmation()
				}
			) {
				Text("Confirm")
			}
		},
		dismissButton = {
			TextButton(
				onClick = {
					onDismissRequest()
				}
			) {
				Text("Dismiss")
			}
		},

	)
}