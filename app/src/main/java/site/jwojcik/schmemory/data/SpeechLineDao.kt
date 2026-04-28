package site.jwojcik.schmemory.data

import androidx.room.*
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeechLineDao {
    @Query("SELECT * FROM SpeechLine WHERE id = :id")
    fun getSpeechLine(id: Long): Flow<SpeechLine?>

    @Query("SELECT * FROM SpeechLine WHERE speechId = :speechId")
    fun getSpeechLines(speechId: Long): Flow<List<SpeechLine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSpeechLine(speechLine: SpeechLine): Long

    @Update
    fun updateSpeechLine(speechLine: SpeechLine)

    @Delete
    fun deleteSpeechLine(speechLine: SpeechLine)
}

