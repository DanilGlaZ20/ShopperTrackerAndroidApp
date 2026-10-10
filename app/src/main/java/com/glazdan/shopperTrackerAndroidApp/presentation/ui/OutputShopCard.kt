package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazdan.shopperTrackerAndroidApp.R

@Composable
fun OutputShopCard(
	modifier: Modifier = Modifier,
	title: String,
	description: String? = null,
	badgeText: String? = null,
	isBought: Boolean = false,
	hasBorder: Boolean = false,
	onCheckedClick: ((Boolean) -> Unit)? = null,
	onRemoveClick: (() -> Unit)? = null,
	onClick: (() -> Unit)? = null

) {
	Card(
		modifier = modifier.fillMaxWidth(),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		onClick = { onClick?.invoke() },

		enabled = onClick != null,
		border = if (hasBorder) BorderStroke(
			width = 1.5.dp,
			color = MaterialTheme.colorScheme.primary
		) else null
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 12.dp, horizontal = 16.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (onCheckedClick != null) {
					Checkbox(
						modifier = Modifier.padding(end = 8.dp),
						checked = isBought,
						onCheckedChange = { onCheckedClick.invoke(it) }


					)
				}

				Text(
					modifier = Modifier.weight(1f),
					text = title,
					fontSize = 18.sp,
					textAlign = TextAlign.Start,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					fontWeight = FontWeight.Bold,
					textDecoration = if (isBought) TextDecoration.LineThrough else TextDecoration.None,
					color = if (isBought) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface

				)
				/*if (onCheckedClick != null) {
					Spacer(modifier = Modifier.width(48.dp))
				}*/
				if (onRemoveClick != null) {
					val interactionSource = remember { MutableInteractionSource() }
					val isPressed by interactionSource.collectIsPressedAsState()
					IconButton(
						onClick = onRemoveClick,
						modifier = Modifier.size(36.dp),
						interactionSource = interactionSource
					) {
						Icon(
							imageVector = Icons.Default.Delete,
							contentDescription = stringResource(R.string.delete_element),
							tint = if (isPressed)  MaterialTheme.colorScheme.primary
							else MaterialTheme.colorScheme.onSurfaceVariant

						)
					}
				}
			}

			if (!description.isNullOrBlank()) {
				Spacer(modifier = Modifier.height(8.dp))
				Text(
					modifier = Modifier.fillMaxWidth(),
					text = description,
					fontSize = 14.sp,
					maxLines = 3,
					textDecoration = if (isBought) TextDecoration.LineThrough else TextDecoration.None,
					overflow = TextOverflow.Ellipsis
				)
			}
			if (!badgeText.isNullOrBlank()) {
				Spacer(modifier = Modifier.padding(8.dp))
				Text(
					modifier = Modifier.fillMaxWidth(),
					text = badgeText,
					textAlign = TextAlign.Right,
					fontSize = 12.sp,
					textDecoration = if (isBought) TextDecoration.LineThrough else TextDecoration.None,
					maxLines = 2,
				)
			}

		}
	}
}

