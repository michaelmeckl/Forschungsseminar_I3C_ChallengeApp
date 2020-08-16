package com.example.challengecovid.model

import java.util.*

/**
 * Simple Version of the Challenge Class, which has only the necessary properties for the UI as not all are needed for display.
 * This can improve performance when used in the ChallengeDAO instead of the normal Challenge class wherever possible.
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 *
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app. These should not be the same as the DTOs.
 */
data class ChallengeUI(
    val title: String,
    val description: String?,
    val iconPath: Int?,
    val difficulty: String
)

/**
 * Convert ui entities to database objects
 */
fun List<ChallengeUI>.asDatabaseModel(): List<Challenge> {
    return map {
        Challenge(
            title = it.title,
            description = it.description,
            iconPath = null,
            points = 3,
            difficulty = "medium",
            duration = 5f,
            startTime = Date().time
        )
    }
}