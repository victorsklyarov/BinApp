package com.zeroillusion.binapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zeroillusion.binapp.presentation.history.components.HistoryScreen
import com.zeroillusion.binapp.presentation.main.components.MainScreen
import com.zeroillusion.binapp.presentation.utils.Screen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "${Screen.MainScreen.route}?bin={bin}?del={del}",
        modifier = modifier
    ) {
        composable("${Screen.MainScreen.route}?bin={bin}?del={del}") { MainScreen(navController) }
        composable(Screen.HistoryScreen.route) { HistoryScreen(navController) }
    }
}