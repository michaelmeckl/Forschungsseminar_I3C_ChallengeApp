package com.example.challengecovid.model

import java.util.UUID

enum class Gender {
    MALE,
    FEMALE,
    DIVERS
}

/*
//Room Model
@Entity(
    tableName = "user_table",
    indices = [Index(value = ["userId"], unique = true)]
)
data class User (
    @PrimaryKey val userId: String = UUID.randomUUID().toString(),
    var username: String = "Anonym",
    val gender: Gender,
    val level: Int,
    val points: Int,
    var userIcon: String,
    val dailyStreakCount: Int
    //val friends: List<User> //necessary ??
    //val currentChallenges: List<Challenge>
)
*/

data class User (
    val userId: String = UUID.randomUUID().toString(),
    val registrationToken: String = "",
    var username: String = "",
    //var gender: Gender = Gender.MALE,
    var level: Int = 1,
    var points: Int = 0,
    var userIcon: String = "",
    var dailyStreakCount: Int = 1,
    val activeChallenges: List<BaseChallenge> = emptyList()
    //val friends: List<User> = emptyList()
)