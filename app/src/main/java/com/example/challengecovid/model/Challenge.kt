package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "challenge_table")
data class Challenge (
    val title: String,
    val description: String?,
    //val icon: Drawable?,   //Drawable cant be saved in room! Only save the path to the resource
    val iconPath: Int?,
    val points: Int,
    val difficulty: String,
    val duration: Float,
    val startTime: Long,
    //val completed: Boolean,   //TODO: necessary?
    @PrimaryKey val challengeId: String = UUID.randomUUID().toString()     //generate a random id
)

/**
 * Map Database Challenges to domain entities (DTO / Data Transfer Object -> Domain Object)
 */
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