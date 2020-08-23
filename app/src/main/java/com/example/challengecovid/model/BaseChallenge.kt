package com.example.challengecovid.model

import androidx.room.PrimaryKey
import java.util.UUID

enum class Difficulty (val points: Int) {
    SCHWER(20),
    MITTEL(10),
    LEICHT(5)
}

abstract class BaseChallenge {
    abstract val title: String
    abstract val difficulty: Difficulty
    abstract val description: String
    abstract val completed: Boolean
    abstract val duration: Int   // for how long this challenge is available (in days)
    var createdAt: Long = System.currentTimeMillis()  //needs to be var so Kotlin automatically generates setter (which is needed for room)
    @PrimaryKey var challengeId: String = UUID.randomUUID().toString()  //generate a random id
}