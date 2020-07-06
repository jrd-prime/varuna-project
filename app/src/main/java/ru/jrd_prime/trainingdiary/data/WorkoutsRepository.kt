package ru.jrd_prime.trainingdiary.data

import ru.jrd_prime.trainingdiary.data.db.WorkoutDao
import ru.jrd_prime.trainingdiary.model.WorkoutModel

class WorkoutsRepository(private val workoutDao: WorkoutDao) {
    fun getWorkouts() = workoutDao.getWorkouts()
    fun insert(workout: WorkoutModel) = workoutDao.insert(workout)
    fun insertAll(workouts: List<WorkoutModel>) = workoutDao.insertAll(workouts)
    fun getWorkout(workoutID: Int) = workoutDao.getWorkout(workoutID)
    fun clearWorkout(workoutID: Int, empty: Boolean) = workoutDao.clearWorkout(workoutID, empty)
    fun restoreWorkout(workoutID: Int, empty: Boolean) = workoutDao.restoreWorkout(workoutID, empty)
    fun getWorkoutsForWeek(start: Long, end: Long) = workoutDao.getWorkoutsForWeek(start, end)
    fun addWorkout(
        workoutID: Int,
        categoryID: Int,
        muscleGroup: String,
        desc: String,
        time: Int,
        empty: Boolean
    ) = workoutDao.addWorkout(workoutID, categoryID, muscleGroup, desc, time, empty)


    companion object {
        @Volatile
        private var instance: WorkoutsRepository? = null

        fun getInstance(workoutDao: WorkoutDao) =
            instance ?: synchronized(this) {
                instance ?: WorkoutsRepository(workoutDao).also { instance = it }
            }
    }
}