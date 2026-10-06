package com.glazdan.shopperTrackerAndroidApp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glazdan.shopperTrackerAndroidApp.data.manager.SessionManager
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization.AuthCommand
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization.AuthViewModel
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization.LoginScreen
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.start.LoadingScreen
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.authorization.RegistrationScreen
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.category.CategoryScreen
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.offline_product.OfflineProductScreen
import com.glazdan.shopperTrackerAndroidApp.presentation.screen.start.StartScreen

@Composable

fun NavGraph(
    sessionManager: SessionManager
) {
    val navController = rememberNavController()
    val userStatus by sessionManager.userStatus.collectAsState(initial = "" )

    if (userStatus == null) return


    NavHost(
        navController = navController,
        startDestination = Screen.Loading.route
    ){

        composable(Screen.Loading.route) {
            LoadingScreen(
                onNavigateNext = {
                    val destination = when (userStatus) {
                        "auth" -> Screen.Category.route
                        "guest" -> Screen.OfflineProduct.route
                        else -> Screen.Start.route
                    }

                    navController.navigate(destination) {
                        popUpTo(Screen.Loading.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Start.route){
            val authViewModel: AuthViewModel = hiltViewModel()
            StartScreen(
                onClickRegisterScreen = {navController.navigate(Screen.Register.route)},
                onClickLoginScreen =  {navController.navigate(Screen.Login.route)},
                onClickGuestCategoryScreen ={
                    authViewModel.processCommand(AuthCommand.GuestBtnClick)
                    navController.navigate(Screen.OfflineProduct.route) {
                        popUpTo(Screen.Start.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route){
            RegistrationScreen(
                onClickRegister = {navController.navigate(Screen.Category.route) {
                    popUpTo(Screen.Start.route) { inclusive = true }
                }},
                onBackClick = {navController.popBackStack()},
                onClickLoginText = {navController.navigate(Screen.Login.route)}
            )
        }
        composable(Screen.Login.route){
            LoginScreen(
                onClickLogin = {navController.navigate(Screen.Category.route) {
                    popUpTo(Screen.Start.route) { inclusive = true }
                }},
                onBackClick = {navController.popBackStack()},
                onClcikRegistrationText = {navController.navigate(Screen.Register.route)}
            )
        }
        composable(Screen.Category.route){
            CategoryScreen(
                modifier = Modifier,
                onBackClick = {navController.navigate(Screen.Start.route)}
            )
        }
        composable(Screen.OfflineProduct.route){

            OfflineProductScreen(
                modifier = Modifier,
                onBackClick = {navController.navigate(Screen.Start.route)}
            )

        }


    }
}

sealed class Screen(val route: String){
    data object Start: Screen("start")
    data object Loading: Screen("loading")
    data object Register: Screen("registration")
    data object Login: Screen("login")
    data object OfflineProduct: Screen("offline_products")

    data object Category: Screen("category")
}