package ru.jrd_prime.trainingdiary.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.getDatesBetweenUsingJava7
import java.text.SimpleDateFormat
import java.util.*

fun initDB(context: Context) {
    val TAG = "INIT DB"
    Log.d(TAG, "Start")
    val list = mutableListOf<WorkoutModel>()

    val startDay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = "01-06-2020"
    val startDayDate: Date = startDay.parse(dateInString)
    val endDay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString2 = "25-07-2020"
    val endDayDate: Date = endDay.parse(dateInString2)
    val dates =
        getDatesBetweenUsingJava7(startDayDate, endDayDate)

    for (date in dates) {
        Log.d(TAG, "List+")
        list.add(WorkoutModel(null, 2, "muscle", "desc", 33, date.time, false))
    }

    runBlocking {
        launch(Dispatchers.IO) {
            WorkoutDatabase.getInstance(context).workoutDao()
                .insertAll(list)
            Log.d(TAG, "initDB: ")

        }
    }
}