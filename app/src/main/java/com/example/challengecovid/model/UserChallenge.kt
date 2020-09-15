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
    override var completed: Boolean,
    override val duration: Int,
    val creatorId: String   // which user created this challenge
) : BaseChallenge()
*/

data class UserChallenge(
    override val challengeId: String = UUID.randomUUID().toString(),    //generate a random id for new user challenges
    override var title: String = "",
    override var description: String = "",
    override var difficulty: Difficulty = Difficulty.LEICHT,
    override val type: ChallengeType = ChallengeType.USER_CHALLENGE,
    override var completed: Boolean = false,
    override var duration: Int = 7,     // in days
    @ServerTimestamp var createdAt: Date? = null,
    var isPublic: Boolean = false,  // whether this challenge is can be seen by others or only by the creator
    val creatorId: String = "",   // which user created this challenge
    val participatingUsers: List<User> = emptyList()
) : BaseChallenge()