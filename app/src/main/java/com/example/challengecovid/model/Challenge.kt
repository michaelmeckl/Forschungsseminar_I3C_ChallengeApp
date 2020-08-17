package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "challenge_table",
    indices = [Index(value = ["title", "description"])]  // speed up queries for titel and description with a composite index
)
data class Challenge(
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean,
    val category: String,
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