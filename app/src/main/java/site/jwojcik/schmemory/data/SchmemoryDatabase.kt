package site.jwojcik.schmemory.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Speech::class, Scene::class, SceneLine::class, SpeechLine::class], version = 1)
abstract class SchmemoryDatabase : RoomDatabase() {

    abstract fun sceneDao(): SceneDao
    abstract fun speechDao(): SpeechDao
    abstract fun sceneLineDao(): SceneLineDao
    abstract fun speechLineDao(): SpeechLineDao
}
