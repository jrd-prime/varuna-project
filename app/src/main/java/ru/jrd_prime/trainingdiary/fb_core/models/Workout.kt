package ru.jrd_prime.trainingdiary.fb_core.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Workout(
    val id: String = "",
    val category: Int = 4,
    var title: String = "",
    var description: String = "",
    var time: Int = 0,
    val date: Long? = 0,
    val additional: HashMap<String, Workout>? = null,
    val calories: String = "",
    var empty: Boolean = true
) {}