package ru.jrd_prime.trainingdiary.utils

import android.util.Log
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.util.*
import kotlin.collections.ArrayList

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

fun dateCut(data: Collection<WorkoutModel>): Collection<WorkoutModel> {
    var newItems = mutableListOf<WorkoutModel>()
    val cal2: Calendar = Calendar.getInstance()
    cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    cal2.add(Calendar.DAY_OF_WEEK, -1)
    val start: Date = cal2.getTime();


    val add1week = cal2.add(Calendar.DAY_OF_WEEK, 7)
    val end = cal2.time


        Log.d("asd2123`23123", "prepData: ${start.toString()}")
        Log.d("asd2123`23123", "prepData: ${end.toString()}")

    for (item in data) {

        if (item.workoutDate in start..end) {
            newItems.add(item)
            Log.d("123123123123", "onCreateViewHolder: ${item.workoutDate}")
        }
    }
    return newItems
}

