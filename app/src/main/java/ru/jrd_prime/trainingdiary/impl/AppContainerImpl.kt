package ru.jrd_prime.trainingdiary.impl

import android.os.Handler
import android.os.Looper
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.data.Repository
import ru.jrd_prime.trainingdiary.data.WorkoutsRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface AppContainer {
    val workoutsRepository: WorkoutsRepository
}

class AppContainerImpl(appContext: TrainingDiaryApp) : AppContainer {
    override val workoutsRepository: WorkoutsRepository by lazy {
        Repository(
            executorService,
            mainThreadHandler
        )
    }
    private val executorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(4)
    }

    private val mainThreadHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
}
