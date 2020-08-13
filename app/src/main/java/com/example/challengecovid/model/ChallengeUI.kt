package com.example.challengecovid.model

/**
 * Simple Version of the Challenge Class, which has only the necessary properties for the UI as not all are needed for display.
 * This can improve performance when used in the ChallengeDAO instead of the normal Challenge class wherever possible.
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
data class ChallengeUI(
    val title: String,
    val description: String?,
    val iconPath: Int?,
    val difficulty: String
)