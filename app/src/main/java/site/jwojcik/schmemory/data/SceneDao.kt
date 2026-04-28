package site.jwojcik.schmemory.data

import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface SceneDao {
    @Query("SELECT * FROM Scene WHERE id = :id")
    fun getScene(id: Long): Flow<Scene?>

    @Query("SELECT * FROM Scene WHERE name LIKE :name ORDER BY id")
    fun sceneSearch(name: String): Flow<List<Scene>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addScene(scene: Scene): Long

    @Update
    fun updateScene(scene: Scene)

    @Delete
    fun deleteScene(scene: Scene)
}

