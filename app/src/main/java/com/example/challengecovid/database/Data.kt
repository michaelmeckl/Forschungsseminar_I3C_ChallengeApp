package com.example.challengecovid.database

import com.example.challengecovid.R
import com.example.challengecovid.model.Challenge
import com.example.challengecovid.model.ChallengeCategory
import com.example.challengecovid.model.Difficulty

/**
 * Simple data class that returns all pre-generated challenges and categories with whom the db is initially populated.
 */
class Data {

    //TODO: wir brauchen dringend bessere Icons als die Standards aus Android Studio!

    companion object {

        // challenge categories
        fun getChallengeCategories(): List<ChallengeCategory> = listOf(
            ChallengeCategory(
                title = "Gesunder Lebensstil",   //TODO: als string ressourcen auslagern?
                description = "Diese Kategorie enthält Challenges, die einen gesunden Lebensstil zum Ziel haben.",
                iconPath = R.drawable.ic_star
            ),
            ChallengeCategory(
                title = "Sport",
                description = "Diese Kategorie enthält Challenges, die Bewegung und körperliche Aktivitäten fördern.",
                iconPath = R.drawable.ic_done
            ),
            ChallengeCategory(
                title = "Entspannen",
                description = "Diese Kategorie enthält Challenges, die für etwas Ruhe und Entspannung im Alltag hilfreich sind.",
                iconPath = R.drawable.ic_statistics
            )
        )

        // (app) challenges
        fun getChallenges(): List<Challenge> = listOf(
            Challenge(
                    "Achte auf deine Mitmenschen",
                    "Versuche dich den heutigen Tag an alle dir bekannten Corona-Umgangsregeln und Richtlinien zu halten, wie beispielsweise 1,50m Abstand zu anderen zu halten, Niesen und Husten nur in Armbeuge und natürlich deine Maske zu tragen, wenn du raus gehst. Damit schützt du deine Mitmenschen und hilfst mit den Virus zu besiegen!",
                    Difficulty.SCHWER,
                    false,
                    "242-fhk24-242",    //TODO: set the category id manually to be able to set this??? or change to title of the category instead of id?
                    1f,     // 1 day
                    R.drawable.ic_message
            ),
            Challenge(
                "Mehr Sport machen",
                "Für 3 Tage hintereinander jeden Tag 10 Liegestütze und 15 Push Ups machen!",
                Difficulty.MITTEL,
                false,
                "category id or title whatever",
                3f,
                R.drawable.ic_star
            ),
            Challenge(
                "Versuche dich diese Woche etwas gesünder zu ernähren",
                "An apple a day, keeps the doctor away!",
                Difficulty.SCHWER,
                false,
                "category this challenge belongs to",
                7f,
                R.drawable.test
            )
        )

    }
}