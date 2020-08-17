package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "challenge_category_table")
data class ChallengeCategory (
    @PrimaryKey val categoryId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val iconPath: Int?
    //val containedChallenges: List<Challenge>  // instead implemented as room relationship table
)