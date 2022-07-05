package com.kerencev.mynasa.model.helpers

import java.time.LocalDate
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
}