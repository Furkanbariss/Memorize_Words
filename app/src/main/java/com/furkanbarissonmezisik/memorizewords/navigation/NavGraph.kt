package com.furkanbarissonmezisik.memorizewords.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furkanbarissonmezisik.memorizewords.ui.screen.AddWordsScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.HomeScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.InfoScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.LearnModeSelectionScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.LearnScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.SettingsScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.WordListScreen
import com.furkanbarissonmezisik.memorizewords.ui.theme.ThemeManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.LanguageManager
import com.furkanbarissonmezisik.memorizewords.ui.theme.BackgroundManager

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddWords : Screen("add_words")
    object Settings : Screen("settings")
    object Info : Screen("info")
    object WordList : Screen("word_list/{listId}?openAddDialog={openAddDialog}") {
        fun createRoute(listId: Long, openAddDialog: Boolean = false) = "word_list/$listId?openAddDialog=$openAddDialog"
    }
    object LearnModeSelection : Screen("learn_mode_selection/{listId}") {
        fun createRoute(listId: Long) = "learn_mode_selection/$listId"
    }
    object Learn : Screen("learn/{listId}?mode={mode}") {
        fun createRoute(listId: Long, mode: String) = "learn/$listId?mode=$mode"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    themeManager: ThemeManager,
    languageManager: LanguageManager,
    backgroundManager: BackgroundManager,
    startDestination: String = Screen.Home.route
) {
    // Navigation state protection
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Prevent rapid navigation changes
            if (destination.route != null) {
                // Navigation state validation without delay
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddWords = {
                    // Prevent multiple rapid navigations
                    if (navController.currentDestination?.route == Screen.Home.route) {
                        navController.navigate(Screen.AddWords.route) {
                            // Prevent multiple instances
                            launchSingleTop = true
                            // Clear back stack to prevent navigation loops
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                },
                onNavigateToLearn = { listId ->
                    if (navController.currentDestination?.route == Screen.Home.route) {
                        navController.navigate(Screen.LearnModeSelection.createRoute(listId)) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                },
                onNavigateToWordList = { listId ->
                    if (navController.currentDestination?.route == Screen.Home.route) {
                        navController.navigate(Screen.WordList.createRoute(listId)) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                },
                onNavigateToSettings = {
                    if (navController.currentDestination?.route == Screen.Home.route) {
                        navController.navigate(Screen.Settings.route) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                },
                onNavigateToInfo = {
                    if (navController.currentDestination?.route == Screen.Home.route) {
                        navController.navigate(Screen.Info.route) {
                            launchSingleTop = true
                            popUpTo(Screen.Home.route) { saveState = true }
                        }
                    }
                }
            )
        }
        
        composable(Screen.AddWords.route) {
            AddWordsScreen(
                onNavigateToWordList = { listId, openAddDialog ->
                    if (navController.currentDestination?.route == Screen.AddWords.route) {
                        navController.navigate(Screen.WordList.createRoute(listId, openAddDialog)) {
                            launchSingleTop = true
                            popUpTo(Screen.AddWords.route) { saveState = true }
                        }
                    }
                },
                onNavigateBack = {
                    // Safe back navigation with state protection
                    if (navController.currentDestination?.route == Screen.AddWords.route) {
                        navController.popBackStack()
                    }
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    if (navController.currentDestination?.route == Screen.Settings.route) {
                        navController.popBackStack()
                    }
                },
                themeManager = themeManager,
                languageManager = languageManager,
                backgroundManager = backgroundManager
            )
        }
        
        composable(Screen.Info.route) {
            InfoScreen(
                onNavigateBack = {
                    if (navController.currentDestination?.route == Screen.Info.route) {
                        navController.popBackStack()
                    }
                }
            )
        }
        
        composable(
            route = Screen.WordList.route,
            arguments = listOf(
                navArgument("listId") { type = androidx.navigation.NavType.LongType },
                navArgument("openAddDialog") { 
                    type = androidx.navigation.NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            val openAddDialog = backStackEntry.arguments?.getBoolean("openAddDialog") ?: false
            
            // Validate arguments before rendering
            if (listId > 0) {
                WordListScreen(
                    listId = listId,
                    openAddDialog = openAddDialog,
                    onNavigateBack = {
                        if (navController.currentDestination?.route?.startsWith("word_list") == true) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
        
        composable(
            route = Screen.LearnModeSelection.route,
            arguments = listOf(
                navArgument("listId") { type = androidx.navigation.NavType.LongType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            
            if (listId > 0) {
                LearnModeSelectionScreen(
                    listId = listId,
                    onNavigateToLearn = { mode ->
                        if (navController.currentDestination?.route?.startsWith("learn_mode_selection") == true) {
                            navController.navigate(Screen.Learn.createRoute(listId, mode.name)) {
                                launchSingleTop = true
                                popUpTo(Screen.LearnModeSelection.createRoute(listId)) { saveState = true }
                            }
                        }
                    },
                    onNavigateBack = {
                        if (navController.currentDestination?.route?.startsWith("learn_mode_selection") == true) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
        
        composable(
            route = Screen.Learn.route,
            arguments = listOf(
                navArgument("listId") { type = androidx.navigation.NavType.LongType },
                navArgument("mode") { 
                    type = androidx.navigation.NavType.StringType
                    defaultValue = "WORD_TO_MEANING"
                }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            val mode = backStackEntry.arguments?.getString("mode") ?: "WORD_TO_MEANING"
            
            if (listId > 0 && mode.isNotEmpty()) {
                LearnScreen(
                    listId = listId,
                    mode = mode,
                    onNavigateBack = {
                        if (navController.currentDestination?.route?.startsWith("learn") == true) {
                            navController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}
