package com.example.challengecovid

import java.util.*

object DateUtil {

    private val calendarInstance = Calendar.getInstance()

    fun getDayOfMonth(): Int {
        return calendarInstance.get(Calendar.DAY_OF_MONTH)
    }
}