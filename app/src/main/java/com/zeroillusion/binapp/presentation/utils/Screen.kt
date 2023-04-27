package com.zeroillusion.binapp.presentation.utils

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object HistoryScreen : Screen("history_screen")
}