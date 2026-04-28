package site.jwojcik.schmemory.data

import android.content.Context

class SceneDataSource (applicationContext: Context?) {
    private var sceneList = listOf<Scene>(
        Scene(
            id = 0,
            name = "Window Scene (Excerpt)",
            readingFor = "Juliet",
            lines = listOf(
                SceneLine("Juliet","Ay, me!"),
                SceneLine("Romeo","She speaks. " +
                        "O, speak again, bright angel, for thou art " +
                        "As glorious to this night, being o'er my head, " +
                        "As is a wingèd messenger of heaven " +
                        "Unto the white upturnèd wond'ring eyes " +
                        "Of mortals that fall back to gaze on him " +
                        "When he bestrides the lazy puffing clouds " +
                        "And sails upon the bosom of the air."),
                SceneLine("Juliet","O Romeo, Romeo! Wherefore art thou Romeo? " +
                        "Deny thy father and refuse thy name; " +
                        "Or, if thou wilt not, be but sworn my love, " +
                        "And I'll no longer be a Capulet."),
                SceneLine("Romeo","Shall I hear more, or shall I speak at this?"),
                SceneLine("Juliet","'Tis but thy name that is my enemy; " +
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
                        "Take all myself."),
                SceneLine("Romeo", "I take thee at thy word. " +
                        "Call me but love, and I'll be new baptized; " +
                        "Henceforth I never will be Romeo."),
                SceneLine("Juliet", "What man art thou that, thus bescreened in night, " +
                        "So stumblest on my counsel?"),
                SceneLine("Romeo", "By a name " +
                        "I know not how to tell thee who I am. " +
                        "My name, dear saint, is hateful to myself, " +
                        "Because it is an enemy to thee. " +
                        "Had I it written, I would tear the word."),
                SceneLine("Juliet", "My ears have not yet drunk a hundred words " +
                        "Of that tongue's uttering, yet I know the sound. " +
                        "Art thou not Romeo and a Montague?"),
                SceneLine("Romeo", "Neither, fair maid, if either thee dislike."),
                SceneLine("Juliet", "How camest thou hither, tell me, and wherefore? " +
                        "The orchard walls are high and hard to climb, " +
                        "And the place death, considering who thou art, " +
                        "If any of my kinsmen find thee here."),
                SceneLine("Romeo", "With love's light wings did I o'erperch these walls, " +
                        "For stony limits cannot hold love out; " +
                        "And what love can do, that dares love attempt. " +
                        "Therefore thy kinsmen are no stop to me."),
                SceneLine("Juliet", "If they do see thee, they will murder thee."),
                SceneLine("Romeo", "Alack, there lies more peril in thine eye " +
                        "Than twenty of their swords. Look thou but sweet, " +
                        "And I am proof against their enmity."),
                SceneLine("Juliet", "I would not for the world they saw thee here."),
                SceneLine("Romeo", "I have night's cloak to hide me from their eyes, " +
                        "And but thou love me, let them find me here. " +
                        "My life were better ended by their hate, " +
                        "Than death proroguèd, wanting of thy love."),
                SceneLine("Juliet", "By whose direction found'st thou out this place?"),
                SceneLine("Romeo", "By love, that first did prompt me to inquire. " +
                        "He lent me counsel, and I lent him eyes. " +
                        "I am no pilot, yet wert thou as far " +
                        "As that vast shore washed with the farthest sea, " +
                        "I would adventure for such merchandise."),
                SceneLine("Juliet", "Thou knowest the mask of night is on my face, " +
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
                        "Which the dark night hath so discoverèd.")
            )
        )
    );

    fun getScene(id: Long): Scene? {
        return sceneList.find { it.id == id }
    }

    fun loadScenes() = sceneList
}