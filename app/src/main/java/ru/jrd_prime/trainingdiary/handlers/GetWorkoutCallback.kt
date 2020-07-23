package ru.jrd_prime.trainingdiary.handlers

import com.google.firebase.database.DataSnapshot
import ru.jrd_prime.trainingdiary.fb_core.models.Workout

interface GetWorkoutCallback {
    fun onCallBack(workout: Workout, workoutID: String)
}

interface GetWorkoutsCallback {
    fun onWorkoutsCallBack(workouts: DataSnapshot)
}