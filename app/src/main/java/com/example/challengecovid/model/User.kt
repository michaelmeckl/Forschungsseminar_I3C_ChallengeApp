package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Gender {
    MALE,
    FEMALE,
    DIVERS
}

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    private val userId: Int,
    private val username: String,
    private val gender: Gender,
    private val level: Int
    //private val friends: List<User> //necessary ??
    //private val currentChallenges: List<Challenge> //TODO needs a M -> N Relationship with (user generated) challenges (see MMusic Playlist -> Song) -> via Foreign Keys in both classes ?
)