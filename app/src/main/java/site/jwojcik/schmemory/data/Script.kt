package site.jwojcik.schmemory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

interface Script {
    var id: Long
    var name: String
    var creationTime: Long
}

interface Line {
    var id: Long
    var text: String
}

@Entity(foreignKeys = [
    ForeignKey(entity = Speech::class,
        parentColumns = ["id"],
        childColumns = ["speechId"],
        onDelete = ForeignKey.CASCADE)
])
data class SpeechLine (
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    var speechId: Long = 0,
    var order: Long = 0,
    override var text: String
) : Line;

@Entity(foreignKeys = [
    ForeignKey(entity = Scene::class,
        parentColumns = ["id"],
        childColumns = ["sceneId"],
        onDelete = ForeignKey.CASCADE)
])
data class SceneLine (
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    var sceneId: Long = 0,
    var order: Long = 0,
    var characterName: String,
    override var text: String
) : Line;

@Entity
data class Scene (
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var name: String,
    var readingFor: String,
    @ColumnInfo(name = "created")
    override var creationTime: Long = System.currentTimeMillis()
) : Script;

@Entity
data class Speech (
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var name: String,
    @ColumnInfo(name = "created")
    override var creationTime: Long = System.currentTimeMillis()
) : Script;