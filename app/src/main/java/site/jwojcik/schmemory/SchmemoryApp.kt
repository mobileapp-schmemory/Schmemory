package site.jwojcik.schmemory

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import site.jwojcik.schmemory.ui.HomeScreen
import site.jwojcik.schmemory.ui.ListScreen
import site.jwojcik.schmemory.ui.SceneScreen
import site.jwojcik.schmemory.ui.SchmemoryListType
import site.jwojcik.schmemory.ui.SpeechScreen

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object SpeechList

    @Serializable
    data class Speech(
        val speechId: Int
    )

    @Serializable
    data object SceneList

    @Serializable
    data class Scene(
        val sceneId: Int
    )

    @Serializable
    data object SettingsList

}

@Composable
fun SchmemoryApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(
                onScenesClick = {
                    navController.navigate(
                        Routes.SceneList
                    )
                },
                onSpeechesClick = {
                    navController.navigate(
                        Routes.SpeechList
                    )
                },
                onSettingsClick = {
                    navController.navigate(
                        Routes.SettingsList
                    )
                }
            )
        }
        composable<Routes.SpeechList> {
            ListScreen(
                listType = SchmemoryListType.SPEECH,
                onUpClick = {
                    navController.navigateUp()
                },
                onItemClick = { speechId: Int ->
                    navController.navigate(
                        Routes.Scene(speechId)
                    )
                }
            )
        }
        composable<Routes.Speech> { backstackEntry ->
            val speech: Routes.Speech = backstackEntry.toRoute()

            SpeechScreen(
                speechId = speech.speechId,
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }

        composable<Routes.SceneList> {
            ListScreen(
                listType = SchmemoryListType.SCENE,
                onUpClick = {
                    navController.navigateUp()
                },
                onItemClick = { sceneId: Int ->
                    navController.navigate(
                        Routes.Scene(sceneId)
                    )
                }
            )
        }
        composable<Routes.Scene> { backstackEntry ->
            val speech: Routes.Scene = backstackEntry.toRoute()

            SceneScreen(
                sceneId = speech.sceneId,
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}