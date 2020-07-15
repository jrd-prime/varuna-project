package ru.jrd_prime.trainingdiary.workers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.impl.AppContainer

class AsyncRequests(private val appContainer: AppContainer) {
    fun updateWorkout(
        workoutID: String,
        newWorkout: Workout
    ) {
        FireBaseCore(appContainer).updateWorkout(workoutID, newWorkout)

        runBlocking {
            launch(Dispatchers.IO) {
//                FireBaseCore(appContainer).addWorkout(
//                    Workout(
//                        workoutID,
//                        newWorkout.category,
//                        newWorkout.title,
//                        newWorkout.description,
//                        newWorkout.time,
//                        newWorkout.date,
//                        false
//                    )
//                )
            }
        }
//        runBlocking {
//            launch(Dispatchers.IO) {
//                appContainer.workoutsRepository.addWorkout(
//                    workoutID,
//                    newWorkout.workoutCategory,
//                    newWorkout.muscleGroup,
//                    newWorkout.desc,
//                    newWorkout.workoutTime,
//                    false
//                )
//            }
//        }
    }

    fun restoreWorkout(workoutID: String) {
        runBlocking {
            launch(Dispatchers.IO) {
//                appContainer.workoutsRepository.restoreWorkout(workoutID, false)
            }
        }
    }

    fun clearWorkout(workoutID: String) {
        runBlocking {
            launch(Dispatchers.IO) {
//                appContainer.workoutsRepository.clearWorkout(workoutID, true)
            }
        }
    }
}