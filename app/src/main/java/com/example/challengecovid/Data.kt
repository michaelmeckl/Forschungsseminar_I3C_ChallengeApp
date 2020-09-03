package com.example.challengecovid

import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.model.ChallengeType
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
            containedChallenges = getHealthyChallenges()    //TODO: effizienter, wenn die als subcollection gespeichert werden?
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
            description = "Diese Kategorie enthält Challenges, die für etwas Ruhe und Entspannung im Alltag sorgen.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_relax_category),
            containedChallenges = getRelaxChallenges()
        ),
        ChallengeCategory(
            categoryId = "Haushalt",
            title = "Haushalt",
            description = "Diese Kategorie enthält Challenges, die für ein angenehmeres Zuhause zum Ziel haben.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_chore_category),
            containedChallenges = getChoresChallenges()
        ),
        ChallengeCategory(
            categoryId = "Vergnügung",
            title = "Vergnügung",
            description = "Diese Kategorie enthält Challenges, die dir etwas gönnen.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_pleasure_category),
            containedChallenges = getPleasureChallenges()
        ),
        ChallengeCategory(
            categoryId = "Produktivität",
            title = "Produktivität",
            description = "Diese Kategorie enthält Challenges, die für die Arbeit motivieren sollen und deine Produktivität steigern.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_productivity_category),
            containedChallenges = getProductivityChallenges()
        ),
        ChallengeCategory(
            categoryId = "Soziales",
            title = "Soziales",
            description = "Diese Kategorie enthält Challenges, die den Kontakt zu anderen Menschen anregen.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_social_category),
            containedChallenges = getSocialChallenges()
        ),
        ChallengeCategory(
            categoryId = "Sicherheit",
            title = "Sicherheit",
            description = "Diese Kategorie enthält Challenges, die das Einhalten der Corona-Maßnahmen erleichtern.",
            categoryIcon = App.instance.resources.getResourceEntryName(R.drawable.ic_safety_category),
            containedChallenges = getSafetyChallenge()
        )
    )

    fun getDailyChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId = "Spazieren",
            title = "Spazieren",
            description = "Geh heute für mindestens 15 Minuten spazieren",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            type = ChallengeType.DAILY_CHALLENGE
        ),
        Challenge(
            challengeId = "Der frühe Vogel",
            title = "Der frühe Vogel",
            description = "Steh morgen etwas früher auf",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            type = ChallengeType.DAILY_CHALLENGE
        ),
        Challenge(
            challengeId = "Früher schlafen gehen",
            title = "Früher schlafen gehen",
            description = "Geh heute abend ein bisschen früher ins Bett als sonst",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            type = ChallengeType.DAILY_CHALLENGE
        ),
        Challenge(
            challengeId = "Kein Fernsehen",
            title = "Kein Fernsehen",
            description = "Verzichte heute komplett auf Fernsehen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            type = ChallengeType.DAILY_CHALLENGE
        ),
        Challenge(
            challengeId = "Kalte Dusche",
            title = "Kalte Dusche",
            description = "Dusche heute nur mit kaltem Wasser",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            type = ChallengeType.DAILY_CHALLENGE
        ),

    )


    //TODO: die icons müssten (eigentlich) noch ausgebessert werden!
    private fun getHealthyChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId="Trinke Wasser!",
            title="Trinke mehr Wasser",
            description ="Versuche für den heutigen Tag nur Wasser als Getränk zu dir zu nehmen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Vegetarisch ernähren",
            title="Vegetarisch ernähren",
            description ="Versuche für diese Woche kein Fleisch zu Essen und dich möglichst vegetarisch zu ernähren",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 7,       //TODO: auch dafür wäre ein Enum hilfreich
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Kein Koffein für eine Woche!",
            title="Kein Koffein für eine Woche!",
            description ="Versuche für die Woche auf jegliche Art von Koffein, also Kaffee, Energydrinks etc., zu versichten",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mehr Obst",
            title="Mehr Obst",
            description ="Versuche für diese Woche zumindest einmal täglich Obst zu essen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Genügend Schlaf",
            title="Genügend Schlaf",
            description ="Achte diese Woche besonders darauf dir genügend Schlaf zu gönnen (min. 7 Stunden täglich)",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getSportChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId="Gehe für 30 Minuten joggen",
            title="Gehe für 30 Minuten joggen",
            description ="Versuche für heute 30 Minuten in deinem Tempo (auch Pausen sind erlaubt) zu joggen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Abendroutine",
            title="Abendroutine",
            description ="Versuche für diese Woche jeden Tag vor dem Schlafen 15 Liegestütze und 15 Sit-ups zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Morgenroutine",
            title="Morgenroutine",
            description ="Versuche für diese Woche jeden Tag nach dem Aufstehen 15 Kniebeugen und 15 Squats zu machen",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Zu Fuß gehen",
            title="Zu Fuß gehen",
            description ="Versuche heute den ganzen Tag möglichst viel zu Fuß zu gehen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Fahrrad fahren",
            title="Fahrrad fahren",
            description ="Versuche diese Woche mit deinem Fahrad zur Arbeit zu fahren. Wenn das nicht möglich ist, versuche zumindest zum Einkaufen nicht das Auto oder den Bus zu nehmen.",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    private fun getRelaxChallenges(): List<Challenge> = listOf(
        Challenge(
            challengeId="Baden",
            title="Baden",
            description ="Geh baden und nimm dir dafür heute so viel Zeit, wie du willst",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Musik",
            title="Musik",
            description ="Nimm dir 30 Minuten, um dir deine Lieblingslieder anzuhören. Danach wirst du dich besser fühlen!",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Meditation",
            title="Meditation",
            description ="Versuche für 30 Minuten zu meditieren.",
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
            description ="Gönn dir heute einen langen Mittagsschlaf, setz dir aber einen Wecker, damit du nicht verschläfst und erst abends aufwachst!",
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
            description ="Ein aufgeräumtes Zimmer sorgt für eine angenehmere Atmosphäre. Wirklich!",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mach dein Bett",
            title="Mach dein Bett",
            description ="Versuche für diese Woche jeden Tag nach dem Aufstehen dein Bett zu machen",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Geschirr waschen",
            title="Geschirr waschen",
            description ="Wasch das schmutzie Geschirr",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Fenster putzen",
            title="Fenster putzen",
            description ="Putz deine Fenster",      //TODO das ist echt wenig, vllt ne bessere Challenge?
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
            description ="Schau dir heute abend deinen Liebliengsfilm an",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Serienjunkie",
            title="Serienjunkie",
            description ="Such dir einen neue Fernsehserie und bingewatche die ersten Staffel(n) ",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 7,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Was du willst",
            title="Was du willst",
            description ="Nimm dir heute 2 Stunden Zeit, um einem deiner Hobbies nachzugehen.",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Lieblingsessen",
            title="Lieblingsessen",
            description ="Koch dir dein Lieblingsessen (oder lass es dir kochen)",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Lesen",
            title="Lesen",
            description ="Nimm dir deinen Nachmittag frei und fang an ein Buch deiner Wahl zu lesen",
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
            description ="Erstell dir einen Wochenplan, in dem du jeden Tag aufschreibst, welche Aufgaben du zu erledigen hast und was du heute geschafft hast",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 7,
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
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 3,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Neues Rezept",
            title="Neues Rezept",
            description ="Such dir ein neues Rezept für ein Gericht und koche es",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 3,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="neue Fähigkeit",
            title="neue Fähigkeit",
            description ="Such und erlerne innerhalb von 5 Tagen eine neue Fähigkeit (egal welche)",
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
            duration = 3,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Small Talk",
            title="Small Talk",
            description ="Ruf oder schreib jemanden an und unterhalte dich mit der Person (einfach nur so)",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Gruppenaktivität",
            title="Gruppenaktivität",
            description ="Verabrede dich mit deinen Freunden/innen und unternehmt, online oder offline, etwas miteinander",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Familie",
            title="Familie",
            description ="Verbringe etwas mehr Zeit mit deiner Familie",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

    //TODO da gehen noch 2 oder 3 :)
    private fun getSafetyChallenge(): List<Challenge> = listOf(
        Challenge(
            challengeId="Händewschen",
            title="Händewschen",
            description ="Achte heute darauf deine Hände für mindestens 20 Sekunden zu waschen, nachdem du draußen warst oder fremde Objekte angefasst hast",
            difficulty = Difficulty.MITTEL,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId="Mundschutz",
            title="Mundschutz",
            description ="Vergiss nicht deine Maske mitzunehmen, wenn du heute rausgehst und versuche dich so gut es geht an die Trageregeln zu halten",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId= "Abstand halten",
            title="Abstand halten",
            description ="Wenn du heute rausgehst, achte darauf, mindestens 1,5m Abstand zu anderen Personen zu halten",
            difficulty = Difficulty.SCHWER,
            completed = false,
            duration = 1,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        ),
        Challenge(
            challengeId= "Niesen / Husten",
            title="Niesen / Husten",
            description ="Achte heute den ganzen Tag bewusst darauf in ein Taschentusch oder zumindest deine Armbeuge zu husten / niesen und dreh dich dabei von anderen weg",
            difficulty = Difficulty.LEICHT,
            completed = false,
            duration = 5,
            challengeIcon = App.instance.resources.getResourceEntryName(R.drawable.icons8_protection_mask_128)
        )
    )

}