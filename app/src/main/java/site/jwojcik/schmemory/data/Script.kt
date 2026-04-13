package site.jwojcik.schmemory.data

interface Script {
    val id: Int
    val name: String
    val lines: List<Line>
}

data class Line (
    val characterName: String,
    val text: String
)

data class Scene (
    override val id: Int = 0,
    override val name: String,
    val readingFor: String,
    override val lines: List<Line>
) : Script;

data class Speech (
    override val id: Int = 0,
    override val name: String,
    override val lines: List<Line>
) : Script;