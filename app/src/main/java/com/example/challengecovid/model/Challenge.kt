package com.example.challengecovid.model

import androidx.room.Entity

@Entity(tableName = "challenge_table")
data class Challenge(
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean,
    val duration: Float?,   //TODO: or interval? or dayOfWeak? How do our challenges look like in the end?
    //val icon: Drawable?,   //Drawable cant be saved in room! Only save the path to the resource
    val iconPath: Int?
) : BaseChallenge()


/**
 * Map Database Challenges to domain entities (DTO / Data Transfer Object -> Domain Object)
 */
/*
fun List<Challenge>.asDomainModel(): List<ChallengeUI> {
    return map {
        ChallengeUI(
            title = it.title,
            description = it.description,
            iconPath = it.iconPath,
            difficulty = it.difficulty
        )
    }
}
*/