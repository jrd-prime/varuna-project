package ru.jrd_prime.trainingdiary.fb_core.models

import android.content.res.Resources
import com.google.firebase.database.IgnoreExtraProperties
import ru.jrd_prime.trainingdiary.R

@IgnoreExtraProperties
data class Workout(
    val id: String = "",
    var category: Int = 4,
    var title: String = "",
    var description: String = "",
    var time: Int = 0,
    val date: Long? = 0,
    val additional: HashMap<String, Workout>? = null,
    val kcal: Int = 0,
    val distance: Float = 0f,
    var empty: Boolean = true
) {
    fun getCheckedTitle(res: Resources): String {
        return if (this.title.isNotEmpty()) this.title else res.getString(R.string.no_title)
    }

    fun getCheckedDescription(res: Resources): String {
        return if (this.description.isNotEmpty()) this.description else res.getString(R.string.no_description)
    }

    fun getFormattedMinutes(res: Resources): String {
        return String.format(res.getString(R.string.minutes_val), this.time.toString())
    }

    fun getFormattedCalories(res: Resources): String {
        return String.format(res.getString(R.string.calories_val), this.kcal.toString())
    }

    fun getFormattedDistance(res: Resources): String {
        return String.format(res.getString(R.string.distance_val), this.distance.toString())
    }

    fun convertMinsToHM(res: Resources): String {
        val timeInt = this.time
        val m = res.getString(R.string.abbr_minute)
        val h = res.getString(R.string.abbr_hour)
        var strTime = ""
        when {
            timeInt < 60 -> return "$timeInt $m"
            timeInt >= 60 -> {
                val minutes = timeInt % 60
                val hours = (timeInt - (timeInt % 60)) / 60
                strTime = if (minutes == 0) "" else "$minutes $m"
                return if (hours == 1) "$hours $h $strTime" else "$hours $h $strTime"
            }
        }
        return strTime
    }
}