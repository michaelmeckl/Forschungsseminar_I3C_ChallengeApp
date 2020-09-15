package com.example.challengecovid.database

/*
class Converters {
    @TypeConverter
    fun fromDifficulty(value: String): Difficulty {
        return enumValueOf(value)
    }

    @TypeConverter
    fun difficultyToString(difficulty: Difficulty): String {
        return difficulty.name
    }

    /*
    @TypeConverter
    fun fromGender(value: String): Gender {
        return enumValueOf(value)
    }

    @TypeConverter
    fun genderToString(gender: Gender): String {
        return gender.name
    }*/

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date): Long {
        return date.time
    }
}

 */