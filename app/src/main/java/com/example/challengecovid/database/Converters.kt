package com.example.challengecovid.database

import androidx.room.TypeConverter
import com.example.challengecovid.model.Difficulty

class Converters {
    @TypeConverter
    fun fromDifficulty(value: String): Difficulty {
        return enumValueOf(value)
    }

    @TypeConverter
    fun difficultyToString(difficulty: Difficulty): String {
        return difficulty.name
    }
}