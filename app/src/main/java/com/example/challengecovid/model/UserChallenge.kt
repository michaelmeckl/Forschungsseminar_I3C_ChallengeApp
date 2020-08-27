package com.example.challengecovid.model

/*
//Room Model
@Entity(tableName = "user_challenge_table")
data class UserChallenge (
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean,
    override val duration: Int,
    val creatorId: String   // which user created this challenge
) : BaseChallenge()
*/

data class UserChallenge (
    override val title: String = "",
    override val description: String = "",
    override val difficulty: Difficulty = Difficulty.LEICHT,
    override val completed: Boolean = false,
    override val duration: Int = 7,     // in days
    val creatorId: String = ""   // which user created this challenge
) : BaseChallenge()