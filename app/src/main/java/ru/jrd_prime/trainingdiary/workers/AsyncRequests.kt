package ru.jrd_prime.trainingdiary.workers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.model.WorkoutModel

class AsyncRequests(private val appContainer: AppContainer) {
    fun updateWorkout(
        workoutID: Int,
        newWorkout: WorkoutModel
    ) {
        runBlocking {
            launch(Dispatchers.IO) {
                appContainer.workoutsRepository.addWorkout(
                    workoutID,
                    newWorkout.workoutCategory,
                    newWorkout.muscleGroup,
                    newWorkout.desc,
                    newWorkout.workoutTime,
                    false
                )
            }
        }
    }

    fun getWorkout(workoutID: Int): WorkoutModel? {
        var data: WorkoutModel? = null
        runBlocking {
            launch(Dispatchers.IO) {
                data = appContainer.workoutsRepository.getWorkout(workoutID)
            }
        }
        return data
    }

    fun restoreWorkout(workoutID: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                appContainer.workoutsRepository.restoreWorkout(workoutID, false)
            }
        }
    }

    fun clearWorkout(workoutID: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                appContainer.workoutsRepository.clearWorkout(workoutID, true)
            }
        }
    }
}