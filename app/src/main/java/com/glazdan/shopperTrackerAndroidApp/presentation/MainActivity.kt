package com.glazdan.shopperTrackerAndroidApp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.glazdan.shopperTrackerAndroidApp.data.manager.SessionManager
import com.glazdan.shopperTrackerAndroidApp.presentation.navigation.NavGraph
import com.glazdan.shopperTrackerAndroidApp.presentation.ui.theme.ShopperTrackerAndroidAppTheme
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopperTrackerAndroidAppTheme {
               NavGraph(sessionManager = sessionManager)

            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopperTrackerAndroidAppTheme {
        Greeting("Android")
    }
}