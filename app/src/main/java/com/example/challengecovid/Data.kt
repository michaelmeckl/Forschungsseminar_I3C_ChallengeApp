package com.example.challengecovid

import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.model.Difficulty

/**
 * Simple data class that returns all pre-generated challenges and categories with whom the db is initially populated.
 */
object Data {

    // challenge categories
    fun getChallengeCategories(): List<ChallengeCategory> = listOf(
        ChallengeCategory(
            categoryId = "Gesunder Lebensstil",
            title = "Gesunder Lebensstil",
            description = "Diese Kategorie enthält Challenges, die einen gesunden Lebensstil zum Ziel haben.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_healthy_category),
            containedChallenges = getHealthyChallenges()
        ),
        ChallengeCategory(
            categoryId = "Sport",
            title = "Sport",
            description = "Diese Kategorie enthält Challenges, die Bewegung und körperliche Aktivitäten fördern.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_sport_category),
            containedChallenges = getSportChallenges()
        ),
        ChallengeCategory(
            categoryId = "Entspannen",
            title = "Entspannen",
            description = "Diese Kategorie enthält Challenges, die für etwas Ruhe und Entspannung im Alltag hilfreich sind.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_relax_category),
            containedChallenges = getRelaxChallenges()
        ),
        ChallengeCategory(
            categoryId = "Haushalt",
            title = "Haushalt",
            description = "Diese Kategorie enthält Challenges, die für ein besseres Zuhause führen sollen  .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_chore_category),
            containedChallenges = getChoresChallenges()
        ),
        ChallengeCategory(
            categoryId = "Vergnügung",
            title = "Vergnügung",
            description = "Diese Kategorie enthält Challenges, die dir etwas gönnen .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_pleasure_category),
            containedChallenges = getPleasureChallenges()
        ),
        ChallengeCategory(
            categoryId = "Produktivität",
            title = "Produktivität",
            description = "Diese Kategorie enthält Challenges, die für die Arbeit motivieren sollen .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_productivity_category),
            containedChallenges = getProductivityChallenges()
        ),
        ChallengeCategory(
            categoryId = "Soziales",
            title = "Soziales",
            description = "Diese Kategorie enthält Challenges, die den Kontakt zu anderen Menschen anregt  .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_social_category),
            containedChallenges = getSocialChallenges()
        ),
        ChallengeCategory(
            categoryId = "Sicherheit",
            title = "Sicherheit",
            description = "Diese Kategorie enthält Challenges, die das Einhalten der Covid-Maßnahmen fördern soll .",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_safety_category),
            containedChallenges = getSafetyChallenge()
        )
    )

    // TODO
    fun getDailyChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId = "Spazieren",
            title = "Spazieren",
            description = "Geh heute für mindestens 15 Minuten spazieren",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            challengeId = "Der frühe Vogel",
            title = "Der frühe Vogel",
            description = "Steh am nächsten Tag früher auf",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            challengeId = "Früher schlafen gehen",
            title = "Früher schlafen gehen",
            description = "Geh heute abend ein bisschen früher ins Bett als sonst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            challengeId = "Kein Fernsehen",
            title = "Kein Fernsehen",
            description = "Verzichte heute komplett aus Fernsehen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.test)
        ),
        Challenge(
            challengeId = "Kalte Dusche",
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
            challengeId="Trinke  Wasser!",
            title="Trinke  Wasser!",
            description ="Versuche für den heutigen Tag nur Wasser als Getränk zu dir zu nehmen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Versuche dich diese Woche vegetarisch zu ernähren",
            title="Versuche dich diese Woche vegetarisch zu ernähren",
            description ="Versuche für diese Woche kein Fleisch zu Essen ",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Versuche für diese Woche auf Koffein zu verzichten",
            title="Versuche für diese Woche auf Koffein zu verzichten",
            description ="Versuche für die Woche auf jegliche Art von Koffein, also Kaffee oder Energydrinks, zu versichten",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Versuche für diese Woche mehr Obst zu Essen",
            title="Versuche für diese Woche mehr Obst zu Essen",
            description ="Versuche für die Woche auf zumindest einmal täglich Obst zu essen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Versuche für diese Woche genügend Schlaf zu bekommen",
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
            challengeId="Gehe für 30 Minuten joggen",
            title="Gehe für 30 Minuten joggen",
            description ="Versuche für heute 30 Minuten in deinem Tempo, auch mit Pausen, zu joggen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Abendroutine",
            title="Abendroutine",
            description ="Versuche für diese Woche vor dem Schlafen 10 Liegestütze und 10 Sit-ups zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Morgenroutine",
            title="Morgenroutine",
            description ="Versuche für diese Woche nach dem Aufstehen 10 Kniebeugen und 10 Squats zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Planks",
            title="Planks",
            description ="Versuche für 3 Sets von 20 Sekunden Planks",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Fahrrad fahren",
            title="Fahrrad fahren",
            description ="Versuche diese Woche mit deinem Fahrad mindestens eine Stunde zu fahren",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getRelaxChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId="Baden",
            title="Baden",
            description ="Lass dir ein Bad ein und verbringe dort so viel Zeit, wie du willst",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Musik",
            title="Musik",
            description ="Hör die 30 Minuten deine Lieblingslieder an",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Meditation",
            title="Meditation",
            description ="Meditiere für 30 Minuten",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Spaziergang",
            title="Spaziergang",
            description ="Geh solange spazieren wie du willst",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mittagsschlaf",
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
            challengeId="Räume dein Zimmer auf",
            title="Räume dein Zimmer auf",
            description ="Ein aufgeräumtes Zimmer macht die Mutter glücklich",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mach dein Bett",
            title="Mach dein Bett",
            description ="Versuche für diese Woche nach dem Aufstehen dein Bett zu machen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Geschirr waschen",
            title="Geschirr waschen",
            description ="Wasch dein Geschirr",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Toilette sauber machen",
            title="Toilette sauber machen",
            description ="",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Fenster putzen",
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
            challengeId="Movienight",
            title="Movienight",
            description ="Schau dir deinen Liebliengsfilm an",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Serienjunkie",
            title="Serienjunkie",
            description ="Such dir einen neuen Fernsehserie und bingewatche die ersten Staffeln ",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Was du willst",
            title="Was du willst",
            description ="Nehm dir heute 2 Stunden Zeit, um eines deiner Hobby nachzugehen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Lieblingsessen",
            title="Lieblingsessen",
            description ="Koch dir oder lass dir dein Lieblingsessen kochen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Lesen",
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
            challengeId="Wochenplan",
            title="Wochenplan",
            description ="Erstell dir einen Wochenplan, wo do aufzeichnest, welche Aufgaben du zu erledigen hast",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Projekt",
            title="Projekt",
            description ="Arbeite an deinem Projekt oder an deiner Hausarbeit weiter",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Stundenplan",
            title="Stundenplan",
            description ="Erstelle dir deinen Stundenplan fürs nächste Semester",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Neues Rezept",
            title="Neues Rezept",
            description ="Such dir ein neues Rezept und koche es",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="neue Fähigkeit",
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
            challengeId="Alter Freund",
            title="Alter Freund",
            description ="Kontaktiere jemanden, mit dem/der du schon lange keinen Kontakt mehr hattest",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Small Talk",
            title="Small Talk",
            description ="Ruf oder schreib jemanden an und unterhalte dich mit der Person",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Gruppenaktivität",
            title="Gruppenaktivität",
            description ="Verabrede dich mit deinen Freunden/innen und unternehmt, online oder offfline, etwas miteinander",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Gerspäch mit deinen Eltern",
            title="Gerspäch mit deinen Eltern",
            description ="Unterhalte dich mit deinen Eltern",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getSafetyChallenge(): List<Challenge> = listOf(
        Challenge(
            challengeId="Händewschen",
            title="Händewschen",
            description ="Wasche deine Hände für mindestens 20 Sekunden, nachddem du fremde Objekte angefasst hast",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mundschutz",
            title="Mundschutz",
            description ="Zieh deine Mundschutzmaske an",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId= "Abstand halten",
            title="Abstand halten",
            description ="Wenn du heute rausgehst, achte darauf, mindestens 1,5m Abstand zu anderen Personen zu halten",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId= "Gesundheit",
            title="Gesundheit",
            description ="Achte heute den ganzen Tag bewusst darauf in ein Taschentusch oder zumindest deine Armbeuge zu husten / niesen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

}