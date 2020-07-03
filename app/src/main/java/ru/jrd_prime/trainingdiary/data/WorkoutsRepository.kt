package ru.jrd_prime.trainingdiary.data

import ru.jrd_prime.trainingdiary.data.db.WorkoutDao

class WorkoutsRepository(private val workoutDao: WorkoutDao) {
    fun getWorkouts() = workoutDao.getWorkouts()

    fun getWorkout(workoutID: Int) = workoutDao.getWorkout(workoutID)

    fun clearWorkout(workoutID: Int, empty: Boolean) = workoutDao.clearWorkout(workoutID, empty)
    fun restoreWorkout(workoutID: Int, empty: Boolean) = workoutDao.restoreWorkout(workoutID, empty)
    fun getWorkoutsForWeek(start: Long, end: Long) =
//        liveData { emit(
        workoutDao.getWorkoutsForWeek(start, end)
//        ) }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: WorkoutsRepository? = null

        fun getInstance(workoutDao: WorkoutDao) =
            instance ?: synchronized(this) {
                instance ?: WorkoutsRepository(workoutDao).also { instance = it }
            }
    }
}


//class WorkoutsRepository(appContext: TrainingDiaryApp) {
//    private var context = appContext
//
//    companion object {
//
//        @Volatile
//        private var instance: WorkoutsRepository? = null
//        final fun getInstance(_context: Context): WorkoutsRepository {
//            if (instance == null) {
//                instance = WorkoutsRepository.getInstance(_context)
//            }
//            return instance as WorkoutsRepository
//        }
//    }
//}