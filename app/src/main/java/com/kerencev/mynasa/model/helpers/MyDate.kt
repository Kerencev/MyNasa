package com.kerencev.mynasa.model.helpers

import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter

object MyDate {
    fun getPastDays(daysToSubtract: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.now()
        val modifiedDate = date.minusDays(daysToSubtract)
        return modifiedDate.format(formatter)
    }

    fun getTime(date: String): String {
        return date.removeRange(0, 10)
    }

    fun mapDateFromDatePickerToCorrectApiDate(year: Int, month: Int, day: Int): String {
        val correctMonth = month + 1
        var yearString = "$year"
        var montString = "$correctMonth"
        var dayString = "$day"
        if (correctMonth < 10) {
            montString = "0$correctMonth"
        }
        if (day < 10) {
            dayString = "0$dayString"
        }
        return "$yearString-$montString-$dayString"
    }
}