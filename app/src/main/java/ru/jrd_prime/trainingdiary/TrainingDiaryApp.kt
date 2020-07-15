package ru.jrd_prime.trainingdiary

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.impl.AppContainerImpl


class TrainingDiaryApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        container = AppContainerImpl(this)
    }
}