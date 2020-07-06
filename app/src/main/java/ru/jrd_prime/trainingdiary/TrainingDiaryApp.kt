package ru.jrd_prime.trainingdiary

import android.app.Application
import android.database.DatabaseUtils
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.jrd_prime.trainingdiary.data.db.WorkoutDatabase
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.impl.AppContainerImpl
import ru.jrd_prime.trainingdiary.utils.DATABASE_NAME


class TrainingDiaryApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        container = AppContainerImpl(this)
    }
}