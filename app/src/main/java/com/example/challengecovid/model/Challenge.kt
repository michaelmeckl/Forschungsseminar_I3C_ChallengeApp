package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_table")
data class Challenge (
    @PrimaryKey(autoGenerate = true) val challengeId: Int = 0,
    val title: String,
    val description: String?,
    //val icon: Drawable?,   //Drawable cant be saved in room! Only save the path to the resource
    val iconPath: Int?,
    val points: Int,
    val difficulty: String,
    val duration: Float,
    val startTime: Long
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