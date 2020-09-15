package com.example.challengecovid.model

enum class Difficulty (val points: Int) {
    SCHWER(20),
    MITTEL(10),
    LEICHT(5)
}

enum class ChallengeType {
    USER_CHALLENGE,
    SYSTEM_CHALLENGE,
    DAILY_CHALLENGE
}

/*
//Room Model
abstract class BaseChallenge {
    abstract val title: String
    abstract val difficulty: Difficulty
    abstract val description: String
    abstract val completed: Boolean
    abstract val duration: Int   // for how long this challenge is available (in days)
    var createdAt: Long = System.currentTimeMillis()  //needs to be var so Kotlin automatically generates setter (which is needed for room)
    @PrimaryKey var challengeId: String = UUID.randomUUID().toString()  //generate a random id
}*/

abstract class BaseChallenge {
    abstract val challengeId: String
    abstract var title: String
    abstract var difficulty: Difficulty
    abstract val type: ChallengeType
    abstract var description: String
    abstract var completed: Boolean
    abstract var duration: Int   // for how long this challenge is available (in days)
}