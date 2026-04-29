package site.jwojcik.schmemory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import site.jwojcik.schmemory.SchmemoryApplication
import site.jwojcik.schmemory.data.Scene
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.data.Speech

class ListScreenViewModel(private val schmRepo: SchmemoryRepository) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                ListScreenViewModel(application.schmemoryRepository)
            }
        }
    }

    private val selectedScenes = MutableStateFlow(emptySet<Scene>())
    private val selectedSpeeches = MutableStateFlow(emptySet<Speech>())

    val uiState: StateFlow<ListScreenUiState> = transformedFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ListScreenUiState(),
        )

    private fun transformedFlow() = combine(
        schmRepo.getScenes(),
        selectedScenes,
        schmRepo.getSpeeches(),
        selectedSpeeches
    ) { scenes, selectScenes, speeches, selectSpeeches ->
        ListScreenUiState(
            sceneList = scenes,
            selectedScenes = selectScenes,
            speechList = speeches,
            selectedSpeeches = selectSpeeches
        )
    }


    fun searchScenes(query: String) = schmRepo.sceneSearch(query)
    fun searchSpeeches(query: String) = schmRepo.speechSearch(query)


    fun addScene(name: String, readingFor: String) {
        schmRepo.addScene(Scene(name = name,readingFor=readingFor))

    }

    fun selectScene(scene: Scene) {
        val selected = selectedScenes.value.contains(scene)
        selectedScenes.value = if (selected) {
            selectedScenes.value.minus(scene)
        } else {
            selectedScenes.value.plus(scene)
        }
    }

    fun hideSceneCab() {
        selectedScenes.value = emptySet()
    }

    fun deleteSelectedScenes() {
        for (scene in selectedScenes.value) {
            schmRepo.deleteScene(scene)
        }
        hideSceneCab()
    }

    fun addSpeech(name: String) {
        schmRepo.addSpeech(Speech(name = name))
    }

    fun selectSpeech(speech: Speech) {
        val selected = selectedSpeeches.value.contains(speech)
        selectedSpeeches.value = if (selected) {
            selectedSpeeches.value.minus(speech)
        } else {
            selectedSpeeches.value.plus(speech)
        }
    }

    fun hideSpeechCab() {
        selectedSpeeches.value = emptySet()
    }

    fun deleteSelectedSpeeches() {
        for (speech in selectedSpeeches.value) {
            schmRepo.deleteSpeech(speech)
        }
        hideSpeechCab()
    }

}

data class ListScreenUiState(
    val sceneList: List<Scene> = emptyList(),
    val selectedScenes: Set<Scene> = emptySet(),
    val speechList: List<Speech> = emptyList(),
    val selectedSpeeches: Set<Speech> = emptySet(),
    val isSceneCabVisible: Boolean = selectedScenes.isNotEmpty(),
    val isSpeechCabVisible: Boolean = selectedSpeeches.isNotEmpty()
)