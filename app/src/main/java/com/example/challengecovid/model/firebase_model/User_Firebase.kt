package com.example.challengecovid.model.firebase_model

data class User_Firebase(
    val registrationToken: String,
    val username: String,
    //val gender: Gender,
    val level: Int,
    val points: Int,
    val userIcon: String,
    val dailyStreakCount: Int,
    val friends: List<User_Firebase>
)