package ru.jrd_prime.trainingdiary.impl

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.gauth.GAuth
import ru.jrd_prime.trainingdiary.utils.AppUtils
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface AppContainer {
    val sharedPreferences: SharedPreferences
    val appConfig: AppConfig
    val appUtils: AppUtils
    val gAuth: GAuth
    val fireDB: FirebaseDatabase
    val fireAuth: FirebaseAuth
}

class AppContainerImpl(private val appContext: TrainingDiaryApp) : AppContainer {

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
    override val fireDB: FirebaseDatabase by lazy {
        Firebase.database
    }
    override val fireAuth: FirebaseAuth by lazy {
        Firebase.auth
    }


    private val executorService: ExecutorService by lazy {
        Executors.newFixedThreadPool(4)
    }

    private val mainThreadHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
}
