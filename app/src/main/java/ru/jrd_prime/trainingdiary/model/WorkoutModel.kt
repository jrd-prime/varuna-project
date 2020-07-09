package ru.jrd_prime.trainingdiary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val workoutID: Int?,
    @ColumnInfo(name = "workout_category")
    val workoutCategory: Int,
    var muscleGroup: String,
    @ColumnInfo(name = "description")
    var desc: String,
    @ColumnInfo(name = "time")
    var workoutTime: Int,
    @ColumnInfo(name = "date")
    val workoutDate: Long?,
    @ColumnInfo(name = "empty")
    val workoutEmpty: Boolean = true
) {}