package ru.jrd_prime.trainingdiary

import android.app.Application
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.impl.AppContainerImpl

class TrainingDiaryApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}