package com.furkanbarissonmezisik.memorizewords.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.furkanbarissonmezisik.memorizewords.ui.screen.AddWordsScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.HomeScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.LearnModeSelectionScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.LearnScreen
import com.furkanbarissonmezisik.memorizewords.ui.screen.WordListScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddWords : Screen("add_words")
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
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAddWords = {
                    navController.navigate(Screen.AddWords.route)
                },
                onNavigateToLearn = { listId ->
                    navController.navigate(Screen.LearnModeSelection.createRoute(listId))
                },
                onNavigateToWordList = { listId ->
                    navController.navigate(Screen.WordList.createRoute(listId))
                }
            )
        }
        
        composable(Screen.AddWords.route) {
            AddWordsScreen(
                onNavigateToWordList = { listId, openAddDialog ->
                    navController.navigate(Screen.WordList.createRoute(listId, openAddDialog))
                },
                onNavigateBack = {
                    navController.popBackStack()
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
            WordListScreen(
                listId = listId,
                openAddDialog = openAddDialog,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.LearnModeSelection.route,
            arguments = listOf(
                navArgument("listId") { type = androidx.navigation.NavType.LongType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getLong("listId") ?: 0L
            LearnModeSelectionScreen(
                listId = listId,
                onNavigateToLearn = { mode ->
                    navController.navigate(Screen.Learn.createRoute(listId, mode.name))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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
            LearnScreen(
                listId = listId,
                mode = mode,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
