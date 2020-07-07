package ru.jrd_prime.trainingdiary.utils

import androidx.room.TypeConverter
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val TAG = "DateHelper.kt"

fun getStartDateForPosition(fragmentPageNumber: Int): Long {
    val today = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN) // today date YYYY-MM-DD
    val thisWeekMonday = today.with(DayOfWeek.MONDAY) // monday of this week
    val weekChanges = (fragmentPageNumber - START_PAGE).toLong() // pages +- = week change
    return when {
        weekChanges > 0 -> dateToTimestamp(thisWeekMonday.plusWeeks(weekChanges)) // + weeks
        weekChanges < 0 -> dateToTimestamp(thisWeekMonday.minusWeeks(abs(weekChanges))) // - weeks
        else -> dateToTimestamp(thisWeekMonday)
    }
}

fun getWeekStartAndEndFromDate(startDate: Long): MutableList<Long> {
    val dates = mutableListOf<Long>()
    dates.add(startDate)
    dates.add(
        dateToTimestamp(
            fromTimestamp(startDate).plusDays(6).toLocalDate().atTime(LocalTime.MAX)
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

@TypeConverter
fun fromTimestamp(value: Long): LocalDateTime {
    return value.let {
        LocalDateTime.ofInstant(
            Instant.ofEpochMilli(value),
            ZoneId.systemDefault()
        )
    }
}

@TypeConverter
fun dateToTimestamp(date: LocalDateTime): Long {
    return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getMonthDayFromDate(date: Date): String {
    return SimpleDateFormat("dd").format(date)
}


fun getDatesBetweenUsingJava7(
    startDate: Date, endDate: Date
): List<Date> {
    val datesInRange: MutableList<Date> = ArrayList()
    val calendar: Calendar = GregorianCalendar()
    calendar.time = startDate
    val endCalendar: Calendar = GregorianCalendar()
    endCalendar.time = endDate
    while (calendar.before(endCalendar)) {
        val result: Date = calendar.time
        datesInRange.add(result)
        calendar.add(Calendar.DATE, 1)
    }
    return datesInRange
}