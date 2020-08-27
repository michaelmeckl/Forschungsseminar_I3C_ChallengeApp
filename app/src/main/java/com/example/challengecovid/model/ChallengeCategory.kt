package com.example.challengecovid.model

/*
//Room Model
@Entity(tableName = "challenge_category_table")
data class ChallengeCategory (
    @PrimaryKey val categoryId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val categoryIcon: String
)
*/

data class ChallengeCategory (
    //val categoryId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val categoryIcon: String,
    val containedChallenges: List<Challenge>
)