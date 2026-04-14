package site.jwojcik.schmemory.data

interface Script {
    val id: Int
    val name: String
    val lines: List<Line>
}

interface Line {
    val text: String
}

data class SpeechLine (
    override val text: String
) : Line;

data class SceneLine (
    val characterName: String,
    override val text: String
) : Line;

data class Scene (
    override val id: Int = 0,
    override val name: String,
    val readingFor: String,
    override val lines: List<SceneLine>
) : Script;

data class Speech (
    override val id: Int = 0,
    override val name: String,
    override val lines: List<SpeechLine>
) : Script;