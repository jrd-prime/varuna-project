package ru.jrd_prime.trainingdiary.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.jrd_prime.trainingdiary.model.WorkoutModel

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts ORDER BY workout_id")
    fun getWorkouts(): List<WorkoutModel>

    @Query("SELECT * FROM workouts WHERE workout_id = :workoutID")
    fun getWorkout(workoutID: Int): WorkoutModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(workout: WorkoutModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(workouts: List<WorkoutModel>)

    @Query("DELETE FROM workouts WHERE workout_id = :workoutID")
    fun delete(workoutID: Int)

    @Query("UPDATE workouts SET empty = :empty WHERE workout_id = :workoutID")
    fun clearWorkout(workoutID: Int, empty: Boolean)

    @Query("UPDATE workouts SET empty = :empty WHERE workout_id = :workoutID")
    fun restoreWorkout(workoutID: Int, empty: Boolean)

    @Query("UPDATE workouts SET empty = :empty, workout_category = :categoryID, muscleGroup = :muscleGroup, description = :desc, time =:time WHERE workout_id = :workoutID")
    fun addWorkout(
        workoutID: Int,
        categoryID: Int,
        muscleGroup: String,
        desc: String,
        time: Int,
        empty: Boolean
    )

    @Query("SELECT * FROM workouts WHERE date >= :startDate AND date < :endDate")
    fun getWorkoutsForWeek(startDate: Long, endDate: Long): LiveData<List<WorkoutModel>>
}