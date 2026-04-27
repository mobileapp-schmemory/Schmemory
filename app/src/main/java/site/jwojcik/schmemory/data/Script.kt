package site.jwojcik.schmemory.data

import kotlinx.serialization.Serializable

interface Script {
    val id: Int
    val name: String
    val lines: List<Line>
}

interface Line {
    val text: String
}

@Serializable
data class SpeechLine (
    override val text: String
) : Line;

@Serializable
data class SceneLine (
    val characterName: String,
    override val text: String
) : Line;

@Serializable
data class Scene (
    override val id: Int = 0,
    override val name: String,
    val readingFor: String,
    override val lines: List<SceneLine>
) : Script;

@Serializable
data class Speech (
    override val id: Int = 0,
    override val name: String,
    override val lines: List<SpeechLine>
) : Script;