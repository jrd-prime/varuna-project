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
    fun getWorkouts(): LiveData<List<WorkoutModel>>

    @Query("SELECT * FROM workouts WHERE workout_id = :workoutID")
    fun getWorkout(workoutID: Int): LiveData<WorkoutModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<WorkoutModel>)
}