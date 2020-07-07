package ru.jrd_prime.trainingdiary.utils

import android.util.Log
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


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