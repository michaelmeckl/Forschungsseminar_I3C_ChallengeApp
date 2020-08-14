package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_created_challenge_table")
data class UserChallenge (
    @PrimaryKey(autoGenerate = true) private val challengeId: Int,
    val title: String,
    val description: String?,
    val iconPath: Int?,
    val points: Int,
    val difficulty: String,
    val duration: Float,
    val startTime: Long
)