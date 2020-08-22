package com.example.challengecovid.database

import androidx.room.TypeConverter
import com.example.challengecovid.model.Difficulty
import com.example.challengecovid.model.Gender

class Converters {
    @TypeConverter
    fun fromDifficulty(value: String): Difficulty {
        return enumValueOf(value)
    }

    @TypeConverter
    fun difficultyToString(difficulty: Difficulty): String {
        return difficulty.name
    }

    @TypeConverter
    fun fromGender(value: String): Gender {
        return enumValueOf(value)
    }

    @TypeConverter
    fun genderToString(gender: Gender): String {
        return gender.name
    }
}