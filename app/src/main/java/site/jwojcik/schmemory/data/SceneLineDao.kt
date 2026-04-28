package site.jwojcik.schmemory.data

import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneLineDao {
    @Query("SELECT * FROM SceneLine WHERE id = :id")
    fun getSceneLine(id: Long): Flow<SceneLine?>

    @Query("SELECT * FROM SceneLine WHERE sceneId = :sceneId")
    fun getSceneLines(sceneId: Long): Flow<SceneLine?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSceneLine(sceneLine: SceneLine): Long

    @Update
    fun updateSceneLine(sceneLine: SceneLine)

    @Delete
    fun deleteSceneLine(sceneLine: SceneLine)
}

