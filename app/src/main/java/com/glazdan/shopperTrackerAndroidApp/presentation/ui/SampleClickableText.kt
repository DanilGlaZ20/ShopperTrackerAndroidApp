@file:Suppress("DEPRECATION")

package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SampleClickableText(
    modifier: Modifier = Modifier,
    notClickableText: String,
    clickableText: String,
    tag: String,
    annotation: String,
    onClick: () -> Unit,
    fontSize: TextUnit = 16.sp
){
    val differentAnnotationText = buildAnnotatedString {
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = fontSize

        )) {
            append(notClickableText)
        }

        pushStringAnnotation(tag = tag, annotation = annotation)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = fontSize
        )) {
            append(clickableText)
        }
        pop()
    }

    ClickableText(
        modifier = modifier,
        text = differentAnnotationText,
        style = TextStyle(fontSize = fontSize),
        onClick = { offset ->
            differentAnnotationText.getStringAnnotations(tag = tag, start = offset, end = offset)
                .firstOrNull()?.let { onClick()}
        }
    )
}




