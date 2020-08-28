package com.example.challengecovid.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

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

data class UserChallenge(
    override val title: String = "",
    override val description: String = "",
    override val difficulty: Difficulty = Difficulty.LEICHT,
    override val type: ChallengeType = ChallengeType.USER_CHALLENGE,
    override val completed: Boolean = false,
    override val duration: Int = 7,     // in days
    @ServerTimestamp val createdAt: Date? = null,
    val isPublic: Boolean = false,  // whether this challenge is can be seen by others or only by the creator
    val creatorId: String = ""   // which user created this challenge
) : BaseChallenge()