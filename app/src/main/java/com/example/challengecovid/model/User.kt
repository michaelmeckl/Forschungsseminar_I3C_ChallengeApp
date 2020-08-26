package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

enum class Gender {
    MALE,
    FEMALE,
    DIVERS
}

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