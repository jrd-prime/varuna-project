package ru.jrd_prime.trainingdiary.utils

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.util.*
import kotlin.math.abs

fun getStartDateForPosition(fragmentPageNumber: Int): String {
    val today = LocalDate.now() // today date YYYY-MM-DD
    val thisWeekMonday = today.with(DayOfWeek.MONDAY) // monday of this week
    val weekChanges = (fragmentPageNumber - START_PAGE).toLong() // pages +- = week change
    return when {
        weekChanges > 0 -> thisWeekMonday.plusWeeks(weekChanges).toString() // + weeks
        weekChanges < 0 -> thisWeekMonday.minusWeeks(abs(weekChanges)).toString() // - weeks
        else -> thisWeekMonday.toString()
    }
}

fun getWeekStartAndEndFromDate(startDate: String): MutableList<String> {
    val dates = mutableListOf<String>()
    dates.add(startDate)
    dates.add(
        startDate.stringToDate().plusDays(6).toString()
    )
    return dates
}

fun Date.dateToString(): String {
    return this.toString()
}

fun String.stringToDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(JP_DATE_FORMAT))
}
