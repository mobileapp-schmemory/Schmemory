package site.jwojcik.schmemory

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.ui.EditScreen
import site.jwojcik.schmemory.ui.HomeScreen
import site.jwojcik.schmemory.ui.ListScreen
import site.jwojcik.schmemory.ui.SchmemoryListType
import site.jwojcik.schmemory.ui.SettingsScreen
import site.jwojcik.schmemory.ui.SpeechScreen
import site.jwojcik.schmemory.ui.SceneScreen

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object SpeechList

    @Serializable
    data class Speech(
        val speechId: Long
    )

    @Serializable
    data class EditSpeech(
        val speechId: Long
    )

    @Serializable
    data object SceneList

    @Serializable
    data class Scene(
        val sceneId: Long
    )

    @Serializable
    data class EditScene(
        val sceneId: Long
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
        composable<Routes.SettingsList> {
            SettingsScreen(
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Routes.SpeechList> {
            ListScreen(
                listType = SchmemoryListType.SPEECH,
                onUpClick = {
                    navController.navigateUp()
                },
                onItemClick = { speechId: Long ->
                    navController.navigate(
                        Routes.Speech(speechId)
                    )
                },
                onEditClick = { speechId: Long ->
                    navController.navigate(
                        Routes.EditSpeech(speechId)
                    )
                }
            )
        }
        composable<Routes.Speech> { backstackEntry ->
            val speech: Routes.Speech = backstackEntry.toRoute()

            SpeechScreen(
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Routes.EditSpeech> { backstackEntry ->
            val editSpeech: Routes.EditSpeech = backstackEntry.toRoute()
            EditScreen(
                scriptId = editSpeech.speechId,
                listType = SchmemoryListType.SPEECH,
                onUpClick = { navController.navigateUp() }
            )
        }

        composable<Routes.SceneList> {
            ListScreen(
                listType = SchmemoryListType.SCENE,
                onUpClick = {
                    navController.navigateUp()
                },
                onItemClick = { sceneId: Long ->
                    navController.navigate(
                        Routes.Scene(sceneId)
                    )
                },
                onEditClick = { sceneId: Long ->
                    navController.navigate(
                        Routes.EditScene(sceneId)
                    )
                }
            )
        }
        composable<Routes.Scene> { backstackEntry ->
            val scene: Routes.Scene = backstackEntry.toRoute()

            SceneScreen(
                onUpClick = {
                    navController.navigateUp()
                }
            )
        }
        composable<Routes.EditScene> { backstackEntry ->
            val editScene: Routes.EditScene = backstackEntry.toRoute()
            EditScreen(
                scriptId = editScene.sceneId,
                listType = SchmemoryListType.SCENE,
                onUpClick = { navController.navigateUp() }
            )
        }
    }
}

class SchmemoryApplication: Application() {
    // Needed to create ViewModels with the ViewModelProvider.Factory
    lateinit var schmemoryRepository: SchmemoryRepository

    // For onCreate() to run, android:name=".StudyHelperApplication" must
    // be added to <application> in AndroidManifest.xml
    override fun onCreate() {
        super.onCreate()
        schmemoryRepository = SchmemoryRepository(this.applicationContext)
    }
}