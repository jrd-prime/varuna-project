package ru.jrd_prime.trainingdiary.utils

import android.widget.TextView
import androidx.room.TypeConverter
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.fb_core.config.DATE_FORMAT_STRING
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

fun getDatesWeekList(startDate: Long): MutableList<String> {
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING)
    val dates = mutableListOf<String>()
    for (i in 0..6) {
        dates.add(fromTimestamp(startDate).plusDays(i.toLong()).format(formatter))
    }
    return dates
}

fun getDatesMonthList(startDate: Long, daysBack: Int): MutableList<String> {
    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING)
    val dates = mutableListOf<String>()
    val start = fromTimestamp(startDate).minusDays(daysBack.toLong())
    for (i in 0..daysBack) {
        dates.add(start.plusDays(i.toLong()).format(formatter))
    }
    return dates
}

fun setDateForHead(view: TextView?) {
    val start = LocalDateTime.now()
    val day = Integer.parseInt(DateTimeFormatter.ofPattern("dd").format(start))
    val month = Integer.parseInt(DateTimeFormatter.ofPattern("MM").format(start))
    val year = Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(start))
    //TODO переписать месяца для ялокализации
    var textMonth = ""
    if (month != 0) {
        textMonth = view?.resources?.getStringArray(R.array.months)?.get(month - 1).toString()
    }
    view?.text = "$day $textMonth $year"
}

fun getWeekFromDate(startDate: Long): MutableList<Long> {
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