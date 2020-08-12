package com.example.challengecovid.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_table")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val challengeId: String,
    val title: String,
    val description: String?,
    val image: Drawable?,
    val points: Int,
    val difficulty: String,
    val time: Float
)