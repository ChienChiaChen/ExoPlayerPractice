package com.chiachen.exoplayerpractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chiachen.exoplayerpractice.presentation.video_list.VideoListScreen
import com.chiachen.exoplayerpractice.presentation.video_player.VideoPlayerScreen
import com.chiachen.exoplayerpractice.ui.theme.ExoPlayerPracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "list") {
                composable("list") {
                    VideoListScreen(navController)
                }

                composable(
                    route = "player?videoUrl={videoUrl}",
                    arguments = listOf(navArgument("videoUrl") {
                        type = NavType.StringType
                        nullable = false
                    })
                ) { backStackEntry ->
                    val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                    VideoPlayerScreen(videoUrl)
                }
            }
//            ExoPlayerPracticeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
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
    ExoPlayerPracticeTheme {
        Greeting("Android")
    }
}