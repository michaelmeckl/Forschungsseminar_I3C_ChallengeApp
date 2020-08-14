package com.example.challengecovid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_category_table")
data class ChallengeCategory (
    @PrimaryKey(autoGenerate = true) val categoryId: Int,
    val title: String,
    val description: String?,
    val iconPath: Int?,
    val containedChallenges: List<Challenge>    //TODO: this can not be inserted into room, needs Serializer like Gson or a TypeConverter
    //TODO: are these challenges even important here? or should they be saved via a Foreign Key in Challenge (1 -> M Relationship)
)