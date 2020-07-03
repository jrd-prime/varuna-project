package ru.jrd_prime.trainingdiary.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.text.SimpleDateFormat
import java.util.*

class DBInitWorker(
    context: Context,
    workerParams: WorkerParameters,
    val database: WorkoutDatabase
) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {


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
            list.add(WorkoutModel(null, 1, "muscle", "desc", 33, date.time, false))
        }

        Log.d(TAG, "Insert")
        val database = WorkoutDatabase.getInstance(applicationContext)
        database.workoutDao().insertAll(list)

        Log.d(TAG, "Ok")


       return Result.success()


    }

}
