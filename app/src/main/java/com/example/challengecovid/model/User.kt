package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class Gender {
    MALE,
    FEMALE,
    DIVERS
}

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey private val userId: String = UUID.randomUUID().toString(),
    private val username: String = "Anonym",
    private val gender: Gender,
    private val level: Int,
    private val points: Int
    //private val iconPath: Int?,
    //private val friends: List<User> //necessary ??
    //private val currentChallenges: List<Challenge> //TODO needs a M -> N Relationship with (user generated) challenges (see MMusic Playlist -> Song) -> via Foreign Keys in both classes ?
)