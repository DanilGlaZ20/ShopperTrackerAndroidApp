package com.glazdan.shopperTrackerAndroidApp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glazdan.shopperTrackerAndroidApp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleTopBar(
    modifier: Modifier = Modifier,
    text: String,
    onBackClick: () -> Unit
){
    CenterAlignedTopAppBar(
        modifier = modifier,

        title = {
            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon =  {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onBackClick() }
                    .padding(8.dp),
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = stringResource(R.string.back_on_previous_activity),
            )
        },

        )

}