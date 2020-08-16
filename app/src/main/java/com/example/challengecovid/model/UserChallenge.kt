package com.example.challengecovid.model

import androidx.room.Entity

@Entity(tableName = "user_challenge_table")
data class UserChallenge (
    override val title: String,
    override val description: String,
    override val difficulty: Difficulty,
    override val completed: Boolean
    //val creator: User   //TODO: necessary? if so how to implement in room? with a foreign key?
    //val iconPath: Int?,
) : BaseChallenge()