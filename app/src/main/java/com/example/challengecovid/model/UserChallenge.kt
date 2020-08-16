package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_created_challenge_table")
data class UserChallenge (
    @PrimaryKey private val challengeId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String?,
    val iconPath: Int?,
    val points: Int,
    val difficulty: String,
    val duration: Float,
    val startTime: Long
)