package ru.jrd_prime.trainingdiary.utils

import android.util.Log
import ru.jrd_prime.trainingdiary.data.prepData
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


fun dateCut2(startDate: Date): Collection<WorkoutModel> {

    val data = prepData()

//    Log.d("asdasdasd", "dateCut2: ${data.toString()}")

    val newItems = mutableListOf<WorkoutModel>()
//    val rr = SimpleDateFormat("yyyy-MM-dd").parse(startDate);

    val cal2: Calendar = Calendar.getInstance()
    cal2.time = startDate
    cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal2.add(Calendar.DAY_OF_WEEK, -1)
    val start: Date = cal2.time;


    val add1week = cal2.add(Calendar.DAY_OF_WEEK, 7)
    val end = cal2.time
//
//
//    Log.d("asd2123`23123", "prepData: ${start.toString()}")
//    Log.d("asd2123`23123", "prepData: ${end.toString()}")

//    for (item in data) {
//
//        if (item.workoutDate in start..end) {
//            newItems.add(item)
////            Log.d("123123123123", "onCreateViewHolder: ${item.workoutDate}")
//        }
//    }
    return newItems
}


fun dateCut(startDate: Long): List<Long> {
    val cal2: Calendar = Calendar.getInstance()
    cal2.time = Date(startDate)
    cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val start: Date = cal2.time;
    cal2.add(Calendar.DAY_OF_WEEK, 6)
    cal2.set(Calendar.HOUR_OF_DAY, cal2.getActualMaximum(Calendar.HOUR_OF_DAY))
    cal2.set(Calendar.MINUTE, cal2.getActualMaximum(Calendar.MINUTE))
    cal2.set(Calendar.SECOND, cal2.getActualMaximum(Calendar.SECOND))
    cal2.set(Calendar.MILLISECOND, cal2.getActualMaximum(Calendar.MILLISECOND))
    val end: Date = cal2.time
    return listOf(start.time, end.time)
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