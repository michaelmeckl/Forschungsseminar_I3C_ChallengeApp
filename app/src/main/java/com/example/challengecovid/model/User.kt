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
    val username: String = "Anonym",
    val gender: Gender,
    val level: Int,
    val points: Int,
    val userIcon: String,
    val dailyStreakCount: Int
    //val friends: List<User> //necessary ??
    //val currentChallenges: List<Challenge>
)
*/

data class User (
    val userId: String = UUID.randomUUID().toString(),
    val registrationToken: String = "",
    val username: String = "",
    //val gender: Gender = Gender.MALE,
    val level: Int = 0,
    val points: Int = 0,
    val userIcon: String = "",
    val dailyStreakCount: Int = 1,
    //val friends: List<User> = emptyList()
)