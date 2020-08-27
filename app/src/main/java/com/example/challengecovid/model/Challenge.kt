package com.example.challengecovid.model

/*
//Room Model
@Entity(
    tableName = "challenge_table",
    indices = [Index(value = ["title", "description"])]  // speed up queries for title and description with a composite index
)
data class Challenge(
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean,
    override val duration: Int,
    val containingCategory: String,     // to which category this challenge belongs
    // IMPORTANT: Don't save the resource identifier for the image as this is generated at compile time and can therefore change!
    val challengeIcon: String
) : BaseChallenge()
*/

data class Challenge(
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean,
    override val duration: Int,
    // IMPORTANT: Don't save the resource identifier for the image as this is generated at compile time and can therefore change!
    val challengeIcon: String
) : BaseChallenge()