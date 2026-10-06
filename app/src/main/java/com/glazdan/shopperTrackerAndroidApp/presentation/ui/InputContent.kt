package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazdan.shopperTrackerAndroidApp.R
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.theme.Critical

@Composable
fun InputContent(
    modifier: Modifier = Modifier,
    text: String,
    query: String?,
    onQueryChanged: (String) -> Unit,
    isPassword: Boolean = false,
    isPhone: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
){
    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {

        if (query != null) {
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth(),
				value = query,
				onValueChange = onQueryChanged,
				colors = OutlinedTextFieldDefaults.colors(
					focusedBorderColor = MaterialTheme.colorScheme.primary,
					unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
					errorBorderColor = Critical
				),
				textStyle = TextStyle(
					fontSize = 18.sp,
					color = MaterialTheme.colorScheme.onSurface
				),
				label = {
					Text(
						text = stringResource(R.string.input_text,
							text),
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold

					)},
				singleLine = true,
				visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,

				prefix = if(isPhone){
					{
						Text(
							text = "+7",
							fontSize = 18.sp,
							color = MaterialTheme.colorScheme.onSurface
						)
					}
				}else null


			)
		}

        Spacer(modifier = Modifier.height(8.dp))

    }

}