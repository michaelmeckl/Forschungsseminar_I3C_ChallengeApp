package com.example.challengecovid.model

import androidx.room.Entity

@Entity(tableName = "user_challenge_table")
data class UserChallenge (
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override var completed: Boolean,
    override val duration: Int,
    val creatorId: String   // which user created this challenge
) : BaseChallenge()