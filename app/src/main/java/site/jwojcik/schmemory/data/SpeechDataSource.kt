package site.jwojcik.schmemory.data

class SpeechDataSource {
    private var speechList = listOf<Speech>(
        Speech(
            id = 0,
            name = "Window Scene (Excerpt, as speech)",
            lines = listOf(
                SpeechLine("Ay, me!"),
                SpeechLine("She speaks.\n" +
                        "O, speak again, bright angel, for thou art\n" +
                        "As glorious to this night, being o'er my head,\n" +
                        "As is a wingèd messenger of heaven\n" +
                        "Unto the white upturnèd wond'ring eyes\n" +
                        "Of mortals that fall back to gaze on him\n" +
                        "When he bestrides the lazy puffing clouds\n" +
                        "And sails upon the bosom of the air."),
                SpeechLine("O Romeo, Romeo! Wherefore art thou Romeo?\n" +
                        "Deny thy father and refuse thy name;\n" +
                        "Or, if thou wilt not, be but sworn my love,\n" +
                        "And I'll no longer be a Capulet."),
                SpeechLine("Shall I hear more, or shall I speak at this?"),
                SpeechLine("'Tis but thy name that is my enemy;\n" +
                        "Thou art thyself, though not a Montague.\n" +
                        "What's Montague? It is nor hand, nor foot,\n" +
                        "Nor arm, nor face, nor any other part\n" +
                        "Belonging to a man. O, be some other name!\n" +
                        "What's in a name? That which we call a rose,\n" +
                        "By any other word would smell as sweet.\n" +
                        "So Romeo would — were he not Romeo called —\n" +
                        "Retain that dear perfection which he owes\n" +
                        "Without that title. Romeo, doff thy name,\n" +
                        "And for that name, which is no part of thee,\n" +
                        "Take all myself."),
                SpeechLine( "I take thee at thy word.\n" +
                        "Call me but love, and I'll be new baptized;\n" +
                        "Henceforth I never will be Romeo."),
                SpeechLine( "What man art thou that, thus bescreened in night,\n" +
                        "So stumblest on my counsel?"),
                SpeechLine( "By a name\n" +
                        "I know not how to tell thee who I am.\n" +
                        "My name, dear saint, is hateful to myself,\n" +
                        "Because it is an enemy to thee.\n" +
                        "Had I it written, I would tear the word."),
                SpeechLine( "My ears have not yet drunk a hundred words\n" +
                        "Of that tongue's uttering, yet I know the sound.\n" +
                        "Art thou not Romeo and a Montague?"),
                SpeechLine( "Neither, fair maid, if either thee dislike."),
                SpeechLine( "How camest thou hither, tell me, and wherefore?\n" +
                        "The orchard walls are high and hard to climb,\n" +
                        "And the place death, considering who thou art,\n" +
                        "If any of my kinsmen find thee here."),
                SpeechLine( "With love's light wings did I o'erperch these walls,\n" +
                        "For stony limits cannot hold love out;\n" +
                        "And what love can do, that dares love attempt.\n" +
                        "Therefore thy kinsmen are no stop to me."),
                SpeechLine( "If they do see thee, they will murder thee."),
                SpeechLine( "Alack, there lies more peril in thine eye\n" +
                        "Than twenty of their swords. Look thou but sweet,\n" +
                        "And I am proof against their enmity."),
                SpeechLine( "I would not for the world they saw thee here."),
                SpeechLine( "I have night's cloak to hide me from their eyes,\n" +
                        "And but thou love me, let them find me here.\n" +
                        "My life were better ended by their hate,\n" +
                        "Than death proroguèd, wanting of thy love."),
                SpeechLine( "By whose direction found'st thou out this place?"),
                SpeechLine( "By love, that first did prompt me to inquire.\n" +
                        "He lent me counsel, and I lent him eyes.\n" +
                        "I am no pilot, yet wert thou as far\n" +
                        "As that vast shore washed with the farthest sea,\n" +
                        "I would adventure for such merchandise."),
                SpeechLine( "Thou knowest the mask of night is on my face,\n" +
                        "Else would a maiden blush bepaint my cheek\n" +
                        "For that which thou hast heard me speak tonight.\n" +
                        "Fain would I dwell on form; fain, fain deny\n" +
                        "What I have spoke. But farewell, compliment\n" +
                        "Dost thou love me? I know thou wilt say 'Ay,'\n" +
                        "And I will take thy word; yet if thou swear'st,\n" +
                        "Thou mayst prove false. At lovers' perjuries\n" +
                        "They say Jove laughs. O gentle Romeo,\n" +
                        "If thou dost love, pronounce it faithfully;\n" +
                        "Or if thou thinkest I am too quickly won,\n" +
                        "I'll frown and be perverse and say thee nay,\n" +
                        "So thou wilt woo, but else not for the world.\n" +
                        "In truth, fair Montague, I am too fond,\n" +
                        "And therefore thou mayst think my behavior light.\n" +
                        "But trust me, gentleman, I'll prove more true\n" +
                        "Than those that have more coying to be strange.\n" +
                        "I should have been more strange, I must confess,\n" +
                        "But that thou overheard'st, ere I was ware,\n" +
                        "My true-love passion. Therefore pardon me,\n" +
                        "And not impute this yielding to light love,\n" +
                        "Which the dark night hath so discoverèd.")
            )
        ),
        Speech(
            id = 2,
            name = "Fart",
            lines = listOf(
                SpeechLine("I farted guys.")
            )
        )
    );

    fun getSpeech(id: Int): Speech? {
        return speechList.find { it.id == id }
    }

    fun loadSpeeches() = speechList
}