package site.jwojcik.schmemory.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchmemoryRepository(context: Context) {

    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            CoroutineScope(Dispatchers.IO).launch {
                addStarterData()
            }
        }
    }

    private val database: SchmemoryDatabase = Room.databaseBuilder(
        context,
        SchmemoryDatabase::class.java,
        "schmemory.db"
    )
        .addCallback(databaseCallback)
        .build()

    private val speechDao = database.speechDao()
    private val sceneDao = database.sceneDao()
    private val speechLineDao = database.speechLineDao()
    private val sceneLineDao = database.sceneLineDao()

    fun getScene(sceneId: Long) = sceneDao.getScene(sceneId)

    fun getScenes() = sceneDao.getScenes()

    fun sceneSearch(name: String) = sceneDao.sceneSearch(name)

    suspend fun addScene(scene: Scene): Long {
        return withContext(Dispatchers.IO) {
            if (scene.name.trim() != "") {
                val id = sceneDao.addScene(scene)
                scene.id = id
                id
            } else 0L
        }
    }

    suspend fun updateScene(scene: Scene) {
        withContext(Dispatchers.IO) {
            sceneDao.updateScene(scene)
        }
    }

    suspend fun deleteScene(scene: Scene) {
        withContext(Dispatchers.IO) {
            sceneDao.deleteScene(scene)
        }
    }

    fun getSpeech(speechId: Long) = speechDao.getSpeech(speechId)

    fun getSpeeches() = speechDao.getSpeeches()

    fun speechSearch(name: String) = speechDao.speechSearch(name)

    suspend fun addSpeech(speech: Speech): Long {
        return withContext(Dispatchers.IO) {
            if (speech.name.trim() != "") {
                val id = speechDao.addSpeech(speech)
                speech.id = id
                id
            } else 0L
        }
    }

    suspend fun updateSpeech(speech: Speech) {
        withContext(Dispatchers.IO) {
            speechDao.updateSpeech(speech)
        }
    }

    suspend fun deleteSpeech(speech: Speech) {
        withContext(Dispatchers.IO) {
            speechDao.deleteSpeech(speech)
        }
    }

    fun getSpeechLine(id: Long) = speechLineDao.getSpeechLine(id)

    fun getSpeechLines(speechId: Long) = speechLineDao.getSpeechLines(speechId)

    suspend fun addSpeechLine(speechLine: SpeechLine): Long {
        return withContext(Dispatchers.IO) {
            val id = speechLineDao.addSpeechLine(speechLine)
            speechLine.id = id
            id
        }
    }

    suspend fun updateSpeechLine(speechLine: SpeechLine) {
        withContext(Dispatchers.IO) {
            speechLineDao.updateSpeechLine(speechLine)
        }
    }

    suspend fun deleteSpeechLine(speechLine: SpeechLine) {
        withContext(Dispatchers.IO) {
            speechLineDao.deleteSpeechLine(speechLine)
        }
    }

    fun getSceneLine(id: Long) = sceneLineDao.getSceneLine(id)

    fun getSceneLines(sceneId: Long) = sceneLineDao.getSceneLines(sceneId)

    suspend fun addSceneLine(sceneLine: SceneLine): Long {
        return withContext(Dispatchers.IO) {
            val id = sceneLineDao.addSceneLine(sceneLine)
            sceneLine.id = id
            id
        }
    }

    suspend fun updateSceneLine(sceneLine: SceneLine) {
        withContext(Dispatchers.IO) {
            sceneLineDao.updateSceneLine(sceneLine)
        }
    }

    suspend fun deleteSceneLine(sceneLine: SceneLine) {
        withContext(Dispatchers.IO) {
            sceneLineDao.deleteSceneLine(sceneLine)
        }
    }


    private suspend fun addStarterData() {
        val windowSceneId = sceneDao.addScene(
            Scene(
                name = "Window Scene (Excerpt)",
                readingFor = "Juliet"
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 0,
                characterName = "Juliet",
                text = "O Romeo, Romeo! Wherefore art thou Romeo? " +
                        "Deny thy father and refuse thy name; " +
                        "Or, if thou wilt not, be but sworn my love, " +
                        "And I'll no longer be a Capulet."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 1,
                characterName = "Romeo",
                text = "Shall I hear more, or shall I speak at this?"
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 2,
                characterName = "Juliet", text = "'Tis but thy name that is my enemy; " +
                        "Thou art thyself, though not a Montague. " +
                        "What's Montague? It is nor hand, nor foot, " +
                        "Nor arm, nor face, nor any other part " +
                        "Belonging to a man. O, be some other name! " +
                        "What's in a name? That which we call a rose, " +
                        "By any other word would smell as sweet. " +
                        "So Romeo would — were he not Romeo called — " +
                        "Retain that dear perfection which he owes " +
                        "Without that title. Romeo, doff thy name, " +
                        "And for that name, which is no part of thee, " +
                        "Take all myself."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 3,
                characterName = "Romeo", text = "I take thee at thy word. " +
                        "Call me but love, and I'll be new baptized; " +
                        "Henceforth I never will be Romeo."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 4,
                characterName = "Juliet",
                text = "What man art thou that, thus bescreened in night, " +
                        "So stumblest on my counsel?"
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 5,
                characterName = "Romeo", text = "By a name " +
                        "I know not how to tell thee who I am. " +
                        "My name, dear saint, is hateful to myself, " +
                        "Because it is an enemy to thee. " +
                        "Had I it written, I would tear the word."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 6,
                characterName = "Juliet",
                text = "My ears have not yet drunk a hundred words " +
                        "Of that tongue's uttering, yet I know the sound. " +
                        "Art thou not Romeo and a Montague?"
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 7,
                characterName = "Romeo",
                text = "Neither, fair maid, if either thee dislike."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 8,
                characterName = "Juliet",
                text = "How camest thou hither, tell me, and wherefore? " +
                        "The orchard walls are high and hard to climb, " +
                        "And the place death, considering who thou art, " +
                        "If any of my kinsmen find thee here."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 9,
                characterName = "Romeo",
                text = "With love's light wings did I o'erperch these walls, " +
                        "For stony limits cannot hold love out; " +
                        "And what love can do, that dares love attempt. " +
                        "Therefore thy kinsmen are no stop to me."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 10,
                characterName = "Juliet",
                text = "If they do see thee, they will murder thee."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 11,
                characterName = "Romeo",
                text = "Alack, there lies more peril in thine eye " +
                        "Than twenty of their swords. Look thou but sweet, " +
                        "And I am proof against their enmity."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 12,
                characterName = "Juliet",
                text = "I would not for the world they saw thee here."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 13,
                characterName = "Romeo",
                text = "I have night's cloak to hide me from their eyes, " +
                        "And but thou love me, let them find me here. " +
                        "My life were better ended by their hate, " +
                        "Than death proroguèd, wanting of thy love."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 14,
                characterName = "Juliet",
                text = "By whose direction found'st thou out this place?"
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 15,
                characterName = "Romeo",
                text = "By love, that first did prompt me to inquire. " +
                        "He lent me counsel, and I lent him eyes. " +
                        "I am no pilot, yet wert thou as far " +
                        "As that vast shore washed with the farthest sea, " +
                        "I would adventure for such merchandise."
            )
        )
        sceneLineDao.addSceneLine(
            SceneLine(
                sceneId = windowSceneId,
                order = 16,
                characterName = "Juliet",
                text = "Thou knowest the mask of night is on my face, " +
                        "Else would a maiden blush bepaint my cheek " +
                        "For that which thou hast heard me speak tonight. " +
                        "Fain would I dwell on form; fain, fain deny " +
                        "What I have spoke. But farewell, compliment " +
                        "Dost thou love me? I know thou wilt say 'Ay,' " +
                        "And I will take thy word; yet if thou swear'st, " +
                        "Thou mayst prove false. At lovers' perjuries " +
                        "They say Jove laughs. O gentle Romeo, " +
                        "If thou dost love, pronounce it faithfully; " +
                        "Or if thou thinkest I am too quickly won, " +
                        "I'll frown and be perverse and say thee nay, " +
                        "So thou wilt woo, but else not for the world. " +
                        "In truth, fair Montague, I am too fond, " +
                        "And therefore thou mayst think my behavior light. " +
                        "But trust me, gentleman, I'll prove more true " +
                        "Than those that have more coying to be strange. " +
                        "I should have been more strange, I must confess, " +
                        "But that thou overheard'st, ere I was ware, " +
                        "My true-love passion. Therefore pardon me, " +
                        "And not impute this yielding to light love, " +
                        "Which the dark night hath so discoverèd."
            )
        )

        val sarahSpeechId = speechDao.addSpeech(
            Speech(
                name = "Sarah"
            )
        )
        speechLineDao.addSpeechLine(
            SpeechLine(
                speechId = sarahSpeechId,
                order = 0,
                text = "Hello, my name is Sarah Todd. " +
                        "I like the color blue, and I like elephants."
            )
        )
        speechLineDao.addSpeechLine(
            SpeechLine(
                speechId = sarahSpeechId,
                order = 1,
                text = "My name is David and I am Sarah's twin. " +
                        "I am super evil MWAHAHAHA"
            )
        )
    }
}
