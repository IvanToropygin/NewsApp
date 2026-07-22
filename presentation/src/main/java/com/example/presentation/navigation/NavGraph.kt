package com.example.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.presentation.screen.settings.SettingsScreen
import com.example.presentation.screen.subscriptions.SubscriptionsScreen
import com.example.presentation.screen.topHeadlines.TopHeadLinesScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.TopHeadlines.route
    ) {
        composable(Screen.TopHeadlines.route) {
            TopHeadLinesScreen(onNavigateToSubscriptions = {
                navController.navigate(Screen.Subscriptions.route)
            })
        }
        composable(Screen.Subscriptions.route) {
            SubscriptionsScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}

sealed class Screen(val route: String) {

    data object TopHeadlines : Screen("topHeadlines")
    data object Subscriptions : Screen("subscriptions")
    data object Settings : Screen("settings")
}