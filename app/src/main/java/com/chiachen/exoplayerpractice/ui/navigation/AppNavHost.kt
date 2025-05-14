package com.chiachen.exoplayerpractice.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chiachen.exoplayerpractice.presentation.downloaded_videos.DownloadedVideosScreen
import com.chiachen.exoplayerpractice.presentation.video_list.VideoListScreen
import com.chiachen.exoplayerpractice.presentation.video_player.VideoPlayerScreen
import com.chiachen.exoplayerpractice.utils.Constants

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screens = listOf(
        Screen.OnlineVideos,
        Screen.DownloadedVideos
    )

    // Check if bottom navigation bar should be shown
    val shouldShowBottomBar = currentRoute?.startsWith(Constants.VIDEO_PLAYER) != true

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavBar(navController = navController, screens = screens)
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Constants.VIDEO_LIST,
            modifier = modifier
        ) {
            composable(Constants.VIDEO_LIST) {
                VideoListScreen(navController = navController)
            }

            composable(Constants.VIDEO_DOWNLOADS) {
                DownloadedVideosScreen(navController = navController)
            }

            composable(
                route = "${Constants.VIDEO_PLAYER}?videoUrl={videoUrl}",
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
}
