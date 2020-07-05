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


// ВОЗВРАЩАЕМ ДАТУ ПОНЕДЕЛЬНИКА В ВЫБРАННОЙ НЕДЕЛЕ
fun calcDateFromPosition(pageNumber: Int): Long {
    val TAG = "calcDateFromPosition"
    val week = Calendar.getInstance()
    week.time = Date()
//    week.set(Calendar.HOUR_OF_DAY, week.getActualMinimum(Calendar.HOUR_OF_DAY))
//    week.set(Calendar.MINUTE, week.getActualMinimum(Calendar.MINUTE))
//    week.set(Calendar.SECOND, week.getActualMinimum(Calendar.SECOND))
//    week.set(Calendar.MILLISECOND, week.getActualMinimum(Calendar.MILLISECOND))
val c = org.threeten.bp.LocalDate.now()


    Log.d(TAG, "cal instance ${c.dayOfMonth}")

    week.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY) // Установили дату на понедельник
    Log.d(TAG, "this week monday ${week.time}")
    week.set(Calendar.HOUR_OF_DAY, week.getActualMinimum(Calendar.HOUR_OF_DAY))
    week.set(Calendar.MINUTE, week.getActualMinimum(Calendar.MINUTE))
    week.set(Calendar.SECOND, week.getActualMinimum(Calendar.SECOND))
    week.set(Calendar.MILLISECOND, week.getActualMinimum(Calendar.MILLISECOND))
    val weekChanged = pageNumber - START_PAGE
    var weekStartDate = week.timeInMillis
    Log.d(TAG, "this week monday ${week.time}")
    if (pageNumber == START_PAGE) {
        /* START PAGE */
        return weekStartDate
    } else {
        /* Считаем разницу и выясняем на сколько недель сдвинут список */
        if (weekChanged > 0) {
            /* Страница увеличилась, значит добавляем недели на сколько сдвинулось */
            week.add(Calendar.WEEK_OF_MONTH, weekChanged)
            weekStartDate = week.timeInMillis
            return weekStartDate
        } else if (weekChanged < 0) {
            /* Страница уменьшилась, значит отнимаем недели */
            week.add(Calendar.WEEK_OF_MONTH, weekChanged)
            weekStartDate = week.timeInMillis
            return weekStartDate
        }
        return weekStartDate
    }
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