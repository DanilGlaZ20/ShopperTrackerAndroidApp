package com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.glazdan.shopperTrackerAndroidApp.R
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.InputContent
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleButton
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleClickableText
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.SampleTopBar

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onClickLogin: (String) -> Unit,
    onBackClick: () -> Unit,
    onClcikRegistrationText: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect( state.token) {
        state.token?.let { token ->
            onClickLogin(token)
        }
    }

    Scaffold{ innerPadding ->

        Column(modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp)){

            SampleTopBar(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.login_topbar),
                onBackClick = onBackClick
            )

            Image(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.im_login_screen),
                contentDescription = "Login illustration",

                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.background,
                    blendMode = BlendMode.Multiply
                )

            )

            InputContent(
                text = stringResource(R.string.username),
                query = state.username,
                onQueryChanged = { viewModel.processCommand(AuthCommand.InputUsername(it)) }
            )

            InputContent(
                text = stringResource(R.string.password),
                query = state.password,
                onQueryChanged = { viewModel.processCommand(AuthCommand.InputPassword(it)) },
                isPassword = true
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            SampleButton(
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                title = stringResource(R.string.login_btn),
                onClick = {viewModel.processCommand(AuthCommand.LoginBtnClick)}
            )

            SampleClickableText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                notClickableText = stringResource(R.string.no_account),
                clickableText = stringResource(R.string.register_button),
                tag = stringResource(R.string.registration_tag),
                annotation = stringResource(R.string.click_tag),
                onClick = onClcikRegistrationText

            )
        }

    }
}




