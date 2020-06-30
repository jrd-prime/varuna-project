package ru.jrd_prime.trainingdiary.utils

import android.util.Log
import ru.jrd_prime.trainingdiary.data.prepData
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.ui.START_PAGE
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// ВОЗВРАЩАЕМ ДАТУ ПОНЕДЕЛЬНИКА В ВЫБРАННОЙ НЕДЕЛЕ
fun calcDateFromPosition(pageNumber: Int): Date {
    val today: Date by lazy {
        Calendar.getInstance().time
    }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd");
    val strDate = dateFormat.format(today);

    val week = Calendar.getInstance()
    week.time = today // Установили текущую дату
    week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Установили дату на понедельник

    val weekChanged = pageNumber - START_PAGE

    var weekStartDate = week.time

    if (pageNumber == START_PAGE) {
        /* START PAGE */
        Log.d("DATE CALC --- ", "calcDateFromPosition: ${weekStartDate.toString()}")
        return weekStartDate
    } else {
        /* Считаем разницу и выясняем на сколько недель сдвинут список */
        if (weekChanged > 0) {
            /* Страница увеличилась, значит добавляем недели на сколько сдвинулось */

            week.add(Calendar.WEEK_OF_MONTH, weekChanged)
            weekStartDate = week.time

            Log.d("DATE CALC --- ", "calcDateFromPosition: ${weekStartDate.toString()}")
            return weekStartDate
        } else if (weekChanged < 0) {
            /* Страница уменьшилась, значит отнимаем недели */
            week.add(Calendar.WEEK_OF_MONTH, weekChanged)
            weekStartDate = week.time
            Log.d("DATE CALC --- ", "calcDateFromPosition: ${weekStartDate.toString()}")
            return weekStartDate
        }

        return weekStartDate
    }
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

fun dateCut(startDate: Date): Collection<WorkoutModel> {

    val data = prepData()

    val newItems = mutableListOf<WorkoutModel>()
//    val rr = SimpleDateFormat("yyyy-MM-dd").parse(startDate);

    val cal2: Calendar = Calendar.getInstance()
    cal2.time = startDate
    cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    cal2.add(Calendar.DAY_OF_WEEK, -1)
    val start: Date = cal2.time;


    val add1week = cal2.add(Calendar.DAY_OF_WEEK, 7)
    val end = cal2.time


    Log.d("asd2123`23123", "prepData: ${start.toString()}")
    Log.d("asd2123`23123", "prepData: ${end.toString()}")

    for (item in data) {

        if (item.workoutDate in start..end) {
            newItems.add(item)
//            Log.d("123123123123", "onCreateViewHolder: ${item.workoutDate}")
        }
    }
    return newItems
}

