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
    override var title: String = "",
    override var description: String = "",
    override var difficulty: Difficulty = Difficulty.LEICHT,
    override var type: ChallengeType = ChallengeType.USER_CHALLENGE,
    override var completed: Boolean = false,
    override var duration: Int = 7,     // in days
    @ServerTimestamp var createdAt: Date? = null,
    var public: Boolean = false,  // whether this challenge is can be seen by others or only by the creator
    var creatorId: String = ""   // which user created this challenge
) : BaseChallenge()