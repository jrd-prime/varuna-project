package ru.jrd_prime.trainingdiary.model

import java.util.*

data class WorkoutModel(
    val workoutID: Int,
    val workoutCategory: Category,
//    val muscleGroup: List<String>,
    val muscleGroup: String,
    val desc: String,
    val workoutTime: Int,
    val workoutDate: Date
)

enum class Category {
    Cardio,
    Stretch,
    Power,
    Rest
}


