package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputShopCard(
	modifier: Modifier = Modifier,
	title: String,
	shopText: String,
	shopDescription: String,
	badgeText: String? = null,
	queryText: String? = null,
	queryDescription: String?,
	queryBadge: String? = null,
	buttonText: String,
	onTextChanged: (String) -> Unit,
	onDescriptionChanged: (String) -> Unit,
	onShopChanged: (String) -> Unit,
	onAddItem: () -> Unit,
){
	Card(
		modifier = modifier,
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Text(
				text = title,
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)

			InputContent(
				text = shopText,
				query = queryText,
				onQueryChanged = onTextChanged
			)

			InputContent(
				text = shopDescription,
				query = queryDescription,
				onQueryChanged = onDescriptionChanged
			)
			if (badgeText != null && queryBadge != null && onShopChanged != null){
				InputContent(
					text = badgeText,
					query = queryBadge,
					onQueryChanged = onShopChanged
				)
			}


			SampleButton(
				modifier = modifier.fillMaxWidth().height(48.dp),
				title = buttonText,
				onClick = onAddItem
			)
		}
	}


}