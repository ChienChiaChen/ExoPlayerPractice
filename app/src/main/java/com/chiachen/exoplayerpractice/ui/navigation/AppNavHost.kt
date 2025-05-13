package com.chiachen.exoplayerpractice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chiachen.exoplayerpractice.presentation.video_list.VideoListScreen
import com.chiachen.exoplayerpractice.presentation.video_player.VideoPlayerScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list",
        modifier = modifier
    ) {
        composable("list") {
            VideoListScreen(navController = navController)
        }

        composable(
            route = "player?videoUrl={videoUrl}",
            arguments = listOf(navArgument("videoUrl") {
                type = NavType.StringType
                nullable = false
            })
        ) { backStackEntry ->
            val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
            VideoPlayerScreen(videoUrl = videoUrl, navController = navController)
        }
    }
}
