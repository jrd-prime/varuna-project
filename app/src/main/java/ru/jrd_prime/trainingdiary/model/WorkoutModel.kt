package ru.jrd_prime.trainingdiary.model

data class WorkoutModel(
    val workoutID: Int,
    val workoutCategory: Category,
    val muscleGroup: List<String>,
    val desc: String,
    val workoutTime: Int,
    val workoutDate: String
) {
    fun gogo() {}
}

enum class Category {
    Cardio,
    Stretch,
    Power,
    Rest
}


