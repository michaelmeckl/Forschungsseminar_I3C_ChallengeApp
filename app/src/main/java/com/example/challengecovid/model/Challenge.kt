package com.example.challengecovid.model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_table")
data class Challenge(
    @PrimaryKey(autoGenerate = true) val challengeId: Int,
    val title: String,
    val description: String?,
    //val icon: Drawable?,   //Drawable cant be saved in room! Only save the path to the resource
    val iconPath: Int?,
    val points: Int,
    val difficulty: String,
    val duration: Float,
    val startTime: Long
)