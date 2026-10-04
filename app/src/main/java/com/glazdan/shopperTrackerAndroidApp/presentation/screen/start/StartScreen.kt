package com.glazdan.shopperTrackerAndroidApp.presentation.screen.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.glazdan.shopperTrackerAndroidApp.R
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleButton
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleClickableText

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onClickRegisterScreen: () -> Unit,
    onClickLoginScreen: () -> Unit,
    onClickGuestCategoryScreen: () -> Unit,
) {
    Scaffold { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 200.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Image(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.im_start_screen),
                contentDescription = "Registration illustration",

                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.background,
                    blendMode = BlendMode.Multiply
                )

            )

            SampleButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                title = stringResource(R.string.register_button),
                onClick = onClickRegisterScreen
            )

            SampleButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                title = stringResource(R.string.login_btn),
                onClick = onClickLoginScreen
            )

            SampleClickableText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                notClickableText = "",
                clickableText = "Использовать для личных целей",
                tag = stringResource(R.string.guest_tag),
                annotation = stringResource(R.string.click_tag),
                onClick = onClickGuestCategoryScreen

            )
        }
    }
}
