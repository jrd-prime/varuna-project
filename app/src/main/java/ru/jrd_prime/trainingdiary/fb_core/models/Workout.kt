package ru.jrd_prime.trainingdiary.fb_core.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Workout(
    val id: String = "",
    val category: Int = 0,
    var title: String = "",
    var description: String = "",
    var time: Int = 0,
    val date: Long? = 0,
    var empty: Boolean = true
) {}