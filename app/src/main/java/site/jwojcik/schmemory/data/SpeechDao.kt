package site.jwojcik.schmemory.data

import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechDao {
    @Query("SELECT * FROM Speech WHERE id = :id")
    fun getSpeech(id: Long): Flow<Speech?>

    @Query("SELECT * FROM Speech ORDER BY id")
    fun getSpeeches(): Flow<List<Speech>>

    @Query("SELECT * FROM Speech WHERE name LIKE :name ORDER BY id")
    fun speechSearch(name: String): Flow<List<Speech>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSpeech(speech: Speech): Long

    @Update
    fun updateSpeech(speech: Speech)

    @Delete
    fun deleteSpeech(speech: Speech)
}

