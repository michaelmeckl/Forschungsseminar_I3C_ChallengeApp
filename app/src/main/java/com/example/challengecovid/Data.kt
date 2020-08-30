package com.example.challengecovid

import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.model.Difficulty

/**
 * Simple data class that returns all pre-generated challenges and categories with whom the db is initially populated.
 */
object Data {

    //TODO: erweitern und passende Icons verwenden!

    //TODO: nur SVG benutzen für categories (keine PNG/JPG oder nur sehr hochauflösende!) sonst sieht die detail view nicht gut aus!

    // challenge categories
    fun getChallengeCategories(): List<ChallengeCategory> = listOf(
        ChallengeCategory(
            title = "Gesunder Lebensstil",
            description = "Diese Kategorie enthält Challenges, die einen gesunden Lebensstil zum Ziel haben.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_healthy_category),
            containedChallenges = getHealthyChallenges()
        ),
        ChallengeCategory(
            title = "Sport",
            description = "Diese Kategorie enthält Challenges, die Bewegung und körperliche Aktivitäten fördern.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_sport_category),
            containedChallenges = getSportChallenges()
        ),
        ChallengeCategory(
            title = "Entspannen",
            description = "Diese Kategorie enthält Challenges, die für etwas Ruhe und Entspannung im Alltag hilfreich sind.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_relax_category),
            containedChallenges = getRelaxChallenges()
        ),
        ChallengeCategory(
            title = "Haushalt",
            description = "Diese Kategorie enthält Challenges, die für ein besseres Zuhause führen sollen  .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_chore_category),
            containedChallenges = getChoresChallenges()
        ),
        ChallengeCategory(
            title = "Vergnügung",
            description = "Diese Kategorie enthält Challenges, die dir etwas gönnen .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_pleasure_category),
            containedChallenges = getPleasureChallenges()
        ),
        ChallengeCategory(
            title = "Produktivität",
            description = "Diese Kategorie enthält Challenges, die für die Arbeit motivieren sollen .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_productivity_category),
            containedChallenges = getProductivityChallenges()
        ),
        ChallengeCategory(
            title = "Soziales",
            description = "Diese Kategorie enthält Challenges, die den Kontakt zu anderen Menschen anregt  .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_social_category),
            containedChallenges = getSocialChallenges()
        ),
        ChallengeCategory(
            title = "Sicherheit",
            description = "Diese Kategorie enthält Challenges, die das Einhalten der Covid-Maßnahmen fördern soll .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_safety_category),
            containedChallenges = getSafetyChallenge()
        )


    )

    //TODO: these challenges should not be used in the categories, instead create own challenges and add them instead of the emptylist!
    // (system) challenges
    fun getDailyChallenges(): List<Challenge> = listOf(
        Challenge(
            title = "Versuche heute alle Corona-Richtlinien einzuhalten",
            description = "Versuche dich den heutigen Tag an alle dir bekannten Corona-Umgangsregeln und Richtlinien zu halten, wie beispielsweise 1,50m Abstand zu anderen zu halten, Niesen und Husten nur in Armbeuge und natürlich deine Maske zu tragen, wenn du raus gehst. Damit schützt du deine Mitmenschen und hilfst mit den Virus zu besiegen!",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title = "Mehr Sport machen",
            description = "Für 3 Tage hintereinander jeden Tag 10 Liegestütze und 15 Push Ups machen!",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 3,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_klopapier)
        ),
        Challenge(
            title = "Versuche dich diese Woche etwas gesünder zu ernähren",
            description = "An apple a day, keeps the doctor away!",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Spazieren",
            description = "Geh für mindestens 15 Minuten Spazieren",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Der frühe Vogel",
            description = "Wach am nächsten Tag früh auf",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Früher schlafen gehen",
            description = "Gehe heue abends früher in bett als sonst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Früher schlafen gehen",
            description = "Gehe heue abends früher in bett als sonst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Früher schlafen gehen",
            description = "Gehe heue abends früher in bett als sonst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Kein Fernsehen",
            description = "Verzichte heute komplett aus Fernsehen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            title = "Kalte Dusche",
            description = "Dusche heute nur mit kaltmen Wasser",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),

    )


    private fun getHealthyChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Trink Wasser!",
            description ="Versuche für den heutigen Tag nur Wasser als Getränk für dich zu nehmen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Versuche dich diese Woche vegetarisch zu ernähren",
            description ="Versuche für diese Woche kein Fleisch zu Essen ",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Versuche für diese Woche auf Koffein zu verzichten",
            description ="Versuche für die Woche auf jegliche Art von Koffein, also Kaffee oder Energydrinks, zu versichten",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Versuche für diese Woche mehr Obst zu Essen",
            description ="Versuche für die Woche auf zumindest einmal täglich Obst zu essen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Versuche für diese Woche genügend Schlaf zu bekommen",
            description ="Versuche für die Woche auf mindestens 8 Stunden Schlaf zu bekommen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getSportChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Gehe für 30 Minuten joggen",
            description ="Versuche für heute 30 Minuten in deinem Tempo, auch mit Pausen, zu joggen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Abendroutine",
            description ="Versuche für diese Woche vor dem Schlafen 10 Liegestütze und 10 Sit-ups zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Morgenroutine",
            description ="Versuche für diese Woche nach dem Aufstehen 10 Kniebeugen und 10 Squats zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Planks",
            description ="Versuche für 3 Sets von 20 Sekunden Planks",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Fahrrad fahren",
            description ="Versuche mit deinem Fahrad eine Stundelang zu fahren",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getRelaxChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Baden",
            description ="Lass dir ein Bad ein und verbringe dort so viel Zeit, wie du willst",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Musik",
            description ="Här die 30 Minuten deine Lieblingslieder an",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Meditation",
            description ="Meditiere für 30 Minuten",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Spaziergang",
            description ="Geh solange spazieren wie du willst",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Mittagsschlaf",
            description ="Geh mitten am Tag schlafen, setz dir aber einen Wecker, damit du nicht zu lange schläfst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getChoresChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Räume dein Zimmer auf",
            description ="Ein aufgeräumtes Zimmer macht die Mutter glücklich",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Mach dein Bett",
            description ="Versuche für diese Woche nach dem Aufstehen dein Bett zu machen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Geschirr waschen",
            description ="Wasch dein Geschirr",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Toilette sauber machen",
            description ="",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Fenster putzen",
            description ="VPutz deine Fenster",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )
    private fun getPleasureChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Movienight",
            description ="Schau dir deinen Liebliengsfilm an",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Serienjunkie",
            description ="Such dir einen neuen Fernsehserie und bingewatche die ersten Staffeln ",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Was du willst",
            description ="Nehm dir heute 2 Stunden Zeit um eines deiner Hobby nachzugehen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Lieblingsessen",
            description ="Koch dir oder lass dir dein Lieblingsessen kochen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Lesen",
            description ="Nimm dir deinen Nachmittag frei und les ein Buch deiner Wahl",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getProductivityChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Wochenplan",
            description ="Erstell dir einen Wochenplan, wo do aufzeichnest, welche Aufgaben du zu erledigen hast",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Projekt",
            description ="Arbeite an deinem Projekt oder an deiner Hausarbeit weiter",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Stundenplan",
            description ="Erstelle dir deinen Stundenplan fürs nächste Semester",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Neues Rezept",
            description ="Such dir ein neues Rezept und koche es",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="neue Fähigkeit",
            description ="Such und erlerne innerhalb von 5 Tagen eine neue Fähigkeit ",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getSocialChallenges(): List<Challenge> = listOf(
        Challenge(
            title="Alter Freund",
            description ="Kontaktiere jemanden, mit dem/der du schon lange keinen Kontakt mehr hattest",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Small Talk",
            description ="Ruf oder schreib jemanden an und unterhalte dich mit der Person",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Gruppenaktivität",
            description ="Verabrde dich mit deinen Freunden/innen und unternehmt, online oder offfline, etwas miteinander",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Gerspäch mit deinen Eltern",
            description ="Unterhalte dich mit deinen Eltern und ",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Gespräch mit jeamden Fremden ",
            description ="Versuche dich mit jemanden zu unterhalten, den du nicht kennst, auch online Über Videospielsürachchats oder Onlineforen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getSafetyChallenge(): List<Challenge> = listOf(
        Challenge(
            title="Händewschen",
            description ="Wasch deine Hände für mindestens 20 Sekunden, nachddem du fremde Objeckte angefasst hast ",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Mundschutz",
            description ="Zieh deine Mundschutzmaske an",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Distanz halten",
            description ="Das nächste mal, wenn du rausgehst, halte mindestens 1,5m Abstand zu anderen Personen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Gesundheit",
            description ="Niese oder huste das nächste mal in deinen Elbogen rein",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            title="Gespräch mit jeamden Fremden ",
            description ="Versuche dich mit jemanden zu unterhalten, den du nicht kennst, auch online Über Videospielsürachchats oder Onlineforen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

}