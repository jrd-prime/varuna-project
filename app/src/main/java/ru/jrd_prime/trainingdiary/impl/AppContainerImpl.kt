package ru.jrd_prime.trainingdiary.impl

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.data.WorkoutsRepository
import ru.jrd_prime.trainingdiary.data.db.WorkoutDao
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import ru.jrd_prime.trainingdiary.gauth.GAuth
import ru.jrd_prime.trainingdiary.utils.AppUtils
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface AppContainer {
    val workoutsRepository: WorkoutsRepository
    val sharedPreferences: SharedPreferences
    val appConfig: AppConfig
    val appUtils: AppUtils
    val gAuth: GAuth
}

class AppContainerImpl(private val appContext: TrainingDiaryApp) : AppContainer {
    override val workoutsRepository: WorkoutsRepository by lazy {
        WorkoutsRepository(workoutDao)
    }
    override val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(AppConfig().getSharedPreferenceName(), Context.MODE_PRIVATE)
    }
    override val appConfig: AppConfig by lazy {
        AppConfig()
    }
    override val appUtils: AppUtils by lazy {
        AppUtils(appContext)
    }
    override val gAuth: GAuth by lazy {
        GAuth(appContext)
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
