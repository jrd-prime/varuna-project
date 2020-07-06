package ru.jrd_prime.trainingdiary.utils

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.util.*
import kotlin.math.abs

const val TAG = "DateHelper.kt"
val jpZoneId = ZoneId.systemDefault()
val convertT = TypeConverterForRoom()

fun getStartDateForPosition(fragmentPageNumber: Int): Long {
    val today = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN) // today date YYYY-MM-DD
//    Log.d(TAG, "today = ${today}")
    val thisWeekMonday = today.with(DayOfWeek.MONDAY) // monday of this week
//    Log.d(TAG, "this monday = $thisWeekMonday")
    val weekChanges = (fragmentPageNumber - START_PAGE).toLong() // pages +- = week change
//    Log.d(TAG, "weekchange = $weekChanges")
    return when {
        weekChanges > 0 -> convertT.dateToTimestamp(thisWeekMonday.plusWeeks(weekChanges)) // + weeks
        weekChanges < 0 -> convertT.dateToTimestamp(thisWeekMonday.minusWeeks(abs(weekChanges)))
        // - weeks
//        else -> thisWeekMonday.toEpochSecond(jpZoneId.rules.getOffset(thisWeekMonday))
        else -> convertT.dateToTimestamp(thisWeekMonday)
    }
}

fun getWeekStartAndEndFromDate(startDate: Long): MutableList<Long> {
    val dates = mutableListOf<Long>()
    dates.add(startDate)
    dates.add(
        convertT.dateToTimestamp(
            convertT.fromTimestamp(startDate).plusDays(6).toLocalDate().atTime(LocalTime.MAX)
        )
    )
    return dates
}

fun Date.dateToString(): String {
    return this.toString()
}

fun String.stringToDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(JP_DATE_FORMAT))
}
//
//fun String.stringToDateInMills() {
//    val zone = ZoneId.systemDefault()
//    this.stringToDate().
//    println(LocalDateTime.now().toEpochSecond(zone.rules.getOffset(LocalDateTime.now())))
//    println(LocalDate.parse("2020-07-11", dtf))
//}