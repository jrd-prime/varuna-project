package ru.jrd_prime.trainingdiary.handlers

import ru.jrd_prime.trainingdiary.fb_core.models.Workout

interface GetAdditionalCallback {
    fun onCallBack(workoutsAdditional: MutableList<Workout>, workoutID: String)
}