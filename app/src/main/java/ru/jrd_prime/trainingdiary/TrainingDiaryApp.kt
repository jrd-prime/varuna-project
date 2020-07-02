package ru.jrd_prime.trainingdiary

import android.app.Application
import android.database.DatabaseUtils
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.impl.AppContainerImpl
import ru.jrd_prime.trainingdiary.utils.DATABASE_NAME


class TrainingDiaryApp : Application() {

    lateinit var container: AppContainer
    lateinit var database: WorkoutDatabase

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)


        val database =
            Room.databaseBuilder<WorkoutDatabase>(this, WorkoutDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
    }

    fun getWorkoutDatabase(): WorkoutDatabase? {
        return database
    }
}