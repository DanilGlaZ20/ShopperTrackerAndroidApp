package com.glazdan.shopperTrackerAndroidApp.presentation.screen.start

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.glazdan.shopperTrackerAndroidApp.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoadingScreen(
	onNavigateNext: () -> Unit
) {
	LaunchedEffect(Unit) {
		delay(2500.milliseconds)
		onNavigateNext()
	}

	val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_cat))
	val progress by animateLottieCompositionAsState(
		composition = composition,
		iterations = LottieConstants.IterateForever
	)

	Box(
		modifier = Modifier
			.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		LottieAnimation(
			composition = composition,
			progress = { progress },
			modifier = Modifier.size(280.dp)
		)
	}
}