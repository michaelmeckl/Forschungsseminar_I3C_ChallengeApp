package com.example.challengecovid.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

// Room Relationships Model
//See https://developer.android.com/training/data-storage/room/relationships#kotlin

/**
 * ################################################
 *  Cross Reference Tables / Associative Entities
 * ################################################
 */

// Associative Entity for the M - N - relationship between users and system challenges
@Entity(
    tableName = "challenge_user_crossref",
    primaryKeys = ["challengeId", "userId"]
)
data class ChallengeUserCrossRef(
    val challengeId: String,
    val userId: String
)

/**
 * ################################################
 *              Relationship Tables
 * ################################################
 */

// 1 - (optional) many - relationship between a user and his own created challenges
data class UserWithChallenges(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "creatorId"
    )
    val createdChallenges: List<UserChallenge>?     // a user doesn't need to have created own challenges so it may be null
)

// 1 - (mandatory) many - relationship between a user and system challenges
data class ChallengesInCategory(
    @Embedded val category: ChallengeCategory,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "containingCategory"
    )
    val associatedChallenges: List<Challenge>  // a challenge category must have at least one challenge belonging to it so it can't be null
)


// 1 - (optional) many - relationship between a user and system challenges
data class ChallengesForUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "challengeId",
        associateBy = Junction(ChallengeUserCrossRef::class)
    )
    val challenges: List<Challenge>?
)

// or the other way round (depending on the need):
// 1 - (optional) many - relationship between a system challenge and users
data class UsersForChallenge(
    @Embedded val challenge: Challenge,
    @Relation(
        parentColumn = "challengeId",
        entityColumn = "userId",
        associateBy = Junction(ChallengeUserCrossRef::class)
    )
    val participatingUsers: List<User>?
)