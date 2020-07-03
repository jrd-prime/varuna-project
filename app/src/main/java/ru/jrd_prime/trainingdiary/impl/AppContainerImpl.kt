package ru.jrd_prime.trainingdiary.impl

import android.os.Handler
import android.os.Looper
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.data.WorkoutsRepository
import ru.jrd_prime.trainingdiary.data.db.WorkoutDao
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface AppContainer {
    val workoutsRepository: WorkoutsRepository
}

class AppContainerImpl(private val appContext: TrainingDiaryApp) : AppContainer {
    override val workoutsRepository: WorkoutsRepository by lazy {
        WorkoutsRepository(workoutDao)
    }
    private val db: WorkoutDatabase by lazy {
        WorkoutDatabase.getInstance(appContext)
    }
    private val workoutDao: WorkoutDao by lazy {
        db.workoutDao()
    }
    private val executorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(4)
    }

    private val mainThreadHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
}
