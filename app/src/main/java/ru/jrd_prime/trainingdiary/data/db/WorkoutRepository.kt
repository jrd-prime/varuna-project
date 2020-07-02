package ru.jrd_prime.trainingdiary.data.db

/**
 * Repository module for handling data operations.
 */
class WorkoutRepository private constructor(private val workoutDao: WorkoutDao) {

    fun getWorkouts() = workoutDao.getWorkouts()

    fun getWorkout(workoutID: Int) = workoutDao.getWorkout(workoutID)


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: WorkoutRepository? = null

        fun getInstance(workoutDao: WorkoutDao) =
            instance ?: synchronized(this) {
                instance ?: WorkoutRepository(workoutDao).also { instance = it }
            }
    }
}
