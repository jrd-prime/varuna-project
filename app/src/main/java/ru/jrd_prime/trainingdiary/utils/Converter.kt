package ru.jrd_prime.trainingdiary.utils

import android.content.res.Resources
import ru.jrd_prime.trainingdiary.R

fun minutesToHoursAndMinutes(timeInt: Int, res: Resources): String {
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