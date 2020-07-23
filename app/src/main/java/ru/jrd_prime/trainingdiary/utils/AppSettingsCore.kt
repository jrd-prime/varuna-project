package ru.jrd_prime.trainingdiary.utils

import android.content.Context
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig

class AppSettingsCore(private val ctx: Context) {
    //todo добавить в меню пункт вида карточек, где можно будет скрыть описание карточек
    companion object {
        const val SHOW_WORKOUT_DESCRIPTION = true
        const val HIDE_CONFIRMATION_FOR_DELETE_MAIN_WORKOUT = false
    }

    fun getShowWorkoutDescription(): Boolean {
        val pref = ctx.getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val value = pref.getBoolean("show_work", false)
        //todo get settings and return

        return value
    }

    fun setTru() {
        val pref = ctx.getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean("show_work", true).apply()
    }

    fun setFalse() {

        val pref = ctx.getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean("show_work", false).apply()
    }


}