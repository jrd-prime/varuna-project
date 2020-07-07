package ru.jrd_prime.trainingdiary.data.db

import android.content.Context
import androidx.room.*
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.DATABASE_NAME


@Database(entities = [WorkoutModel::class], version = 1, exportSchema = false)
//@TypeConverters(TypeConverterForRoom::class)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: WorkoutDatabase? = null

        final fun getInstance(context: Context): WorkoutDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, WorkoutDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration()
                        .build()
            }
            return instance as WorkoutDatabase
        }
    }

//    companion object {
//
//        // For Singleton instantiation
//        @Volatile
//        private var instance: WorkoutDatabase? = null
//
//        fun getInstance(context: Context): WorkoutDatabase {
//            return instance ?: synchronized(this) {
//                Log.d("DB!", "\n\nDBINSTANCE\n\n")
//                instance ?: buildDatabase(context).also { instance = it }
//
//            }
//        }

    // Create and pre-populate the database. See this article for more details:
    // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
//        private fun buildDatabase(context: Context): WorkoutDatabase {
//            Log.d("TAG", "onCreate: DB1")
//            return Room.databaseBuilder(context, WorkoutDatabase::class.java, DATABASE_NAME)
//                .addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        val request = OneTimeWorkRequest.Builder(DBInitWorker::class.java).build()
//                        WorkManager.getInstance(context).enqueue(request)
//                    }
//                }).allowMainThreadQueries()
//                .build()
//        }
}
