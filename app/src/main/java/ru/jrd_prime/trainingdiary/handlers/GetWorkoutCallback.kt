package ru.jrd_prime.trainingdiary.handlers

import ru.jrd_prime.trainingdiary.fb_core.models.Workout

interface GetWorkoutCallback {
    fun onCallBack(workout: Workout, workoutID: String)
}