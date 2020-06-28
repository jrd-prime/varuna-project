package ru.jrd_prime.trainingdiary.data

import android.os.Handler
import java.util.concurrent.ExecutorService

class Repository(
    executorService: ExecutorService,
    mainThreadHandler: Handler
) : WorkoutsRepository{
    override fun getWorkout() {
        TODO("Not yet implemented")
    }

    override fun setWorkout() {
        TODO("Not yet implemented")
    }

    override fun getWorkouts() {
        TODO("Not yet implemented")
    }
}