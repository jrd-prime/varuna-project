package ru.jrd_prime.trainingdiary.fb_core

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import org.threeten.bp.LocalDateTime
import ru.jrd_prime.trainingdiary.adapter.WorkoutListAdapter
import ru.jrd_prime.trainingdiary.fb_core.config._CATEGORIES
import ru.jrd_prime.trainingdiary.fb_core.config._USERS
import ru.jrd_prime.trainingdiary.fb_core.config._WORKOUTS
import ru.jrd_prime.trainingdiary.fb_core.models.Category
import ru.jrd_prime.trainingdiary.fb_core.models.User
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.handlers.GetWorkoutCallback
import ru.jrd_prime.trainingdiary.impl.AppContainer

class FireBaseCore(private val appContainer: AppContainer) {
    companion object {
        const val TAG = "FireBaseCore"
    }

    private val db = appContainer.fireDB
    private val userRef = db.getReference(_USERS)
    private val categoriesRef = db.getReference(_CATEGORIES)
    private val workoutsRef = db.getReference(_WORKOUTS)
    private val userId = appContainer.sharedPreferences.getString("jp_uid", "").toString()
    private val today = LocalDateTime.now()
    private val year = today.year.toString()
    private val month = today.monthValue.toString()
    private val woRef = workoutsRef.child(userId)

    private fun workoutPathConstructor(workoutId: String): DatabaseReference {
        val splitDate = workoutId.split("-")
        val year = splitDate[0]
        val month = splitDate[1]
        return woRef.child(year).child(month)
    }

    private fun addWorkoutWithId(workoutId: String, workout: Workout) {
        val actualRef = workoutPathConstructor(workoutId)
        actualRef.child(workoutId).setValue(workout)
    }

    fun addMoreWorkout(workoutDate: String, workout: Workout) {
        val splitDate = workoutDate.split("-")
        val year = splitDate[0]
        val month = splitDate[1]
        val day = splitDate[2]
        val key = woRef.child(year).child(month).child(workoutDate).child("additional").push().key
        if (key == null) {
            Log.w(TAG, "Couldn't get push key for posts")
            return
        }
        woRef.child(year).child(month).child(workoutDate).child("additional").child(key)
            .setValue(workout)
    }
    fun updateExtraWorkout(workoutDate: String, workout: Workout, key: String) {
        val splitDate = workoutDate.split("-")
        val year = splitDate[0]
        val month = splitDate[1]
        val day = splitDate[2]

        woRef.child(year).child(month).child(workoutDate).child("additional").child(key)
            .setValue(workout)
    }
    fun pushCategories() {
        val category = listOf<Category>(
            Category(1, "cardio"),
            Category(2, "power"),
            Category(3, "stretch"),
            Category(4, "rest")
        )
        for (cat in category) {
            categoriesRef.child(cat.id.toString()).setValue(cat.name)
        }
    }

    fun addNewUserOnSignIn(uid: String?, name: String?, mail: String?) {
        val user = User(uid, name, mail, "")
        if (uid != null) {
            userRef.child(uid).setValue(user)
        }
    }


    /*
    * 1. Берем список дат в виде массива дд-мм-гггг из 7ми дат
    * 2. Читаем данные посписку с этими датами
    * 3. При чтении даты, если ее нет - записываем пустой ворк в эту дату
    * 4. Добавляем пустой ворк в список для отдачи листу
    *
    *
    * */


    fun getWeekData(
        dates: List<String>,
        myNewAdapter: WorkoutListAdapter
    ) {
        val weekData = mutableListOf<Workout>()
        for (date in dates) {
            val actualRef = workoutPathConstructor(date)

            val dateData = actualRef.child(date)

            dateData.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    var workout = snapshot.getValue<Workout>()
                    if (workout != null) {
                        weekData.add(workout) // Если воркаут не пуст, то добавляем в даталист
                    } else {
                        val newWorkout = Workout(id = date)
                        workout = newWorkout
                        weekData.add(newWorkout) // Добавляем пустой воркаут с ИД в даталист
                        addWorkoutWithId(
                            workoutId = date,
                            workout = newWorkout
                        ) // Добавляем воркаут в БД
                    }
                    myNewAdapter.addItem(workout)
                }

            })
        }
    }


    private fun getEmptyWorkout(date: String): Workout {
        return Workout(id = date)
    }


    fun listenNewData(myNewAdapter: WorkoutListAdapter) {
        //TODO get current year and month for listen
        val t = woRef.child("2020").child("07")
        t.addChildEventListener(object : ChildEventListener {
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                val workout = snapshot.getValue<Workout>()
                if (workout != null && key != null) myNewAdapter.updateItem(key, workout)
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }

    private fun addEmptyWorkout(
        t: DatabaseReference,
        date: String
    ): Workout {
        t.setValue(Workout(id = date))
        return Workout()
    }
//    val id: String = "",
//    val category: Int = 0,
//    var title: String = "",
//    var description: String = "",
//    var time: Int = 0,
//    val date: Long? = 0,
//    val additional: HashMap<String, Workout>? = null,
//    val calories: String = "",
//    var empty: Boolean = true
    fun updateWorkout(workoutID: String, newWorkout: Workout) {
        val actualRef = workoutPathConstructor(workoutID)
        val dateData = actualRef.child(workoutID)
        dateData.child("category").setValue(newWorkout.category)
        dateData.child("title").setValue(newWorkout.title)
        dateData.child("description").setValue(newWorkout.description)
        dateData.child("time").setValue(newWorkout.time)
//        dateData.child("calories").setValue(newWorkout.calories) //todo inser calories support

    }


    fun getWorkout(getWorkoutCallback: GetWorkoutCallback, workoutID: String) {
        val actualRef = workoutPathConstructor(workoutID)
        val dateData = actualRef.child(workoutID)

        dateData.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val wo = snapshot.getValue<Workout>()
                if (wo != null) {
                    getWorkoutCallback.onCallBack(workout = wo, workoutID = snapshot.key.toString())
                }
            }
        })
    }

    fun getExtraWorkout(getWorkoutCallback: GetWorkoutCallback, workoutID: String, key: String) {
        val actualRef = workoutPathConstructor(workoutID)
        val dateData = actualRef.child(workoutID).child("additional").child(key)

        dateData.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val wo = snapshot.getValue<Workout>()
                if (wo != null) {
                    getWorkoutCallback.onCallBack(workout = wo, workoutID = snapshot.key.toString())
                }
            }
        })
    }



    fun clearWorkout(workoutID: String) {
        val actualRef = workoutPathConstructor(workoutID)
        val dateData = actualRef.child(workoutID)
        dateData.child("empty").setValue(true)
    }

    fun restoreWorkout(workoutID: String) {
        val actualRef = workoutPathConstructor(workoutID)
        val dateData = actualRef.child(workoutID)
        dateData.child("empty").setValue(false)
    }
}