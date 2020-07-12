package ru.jrd_prime.trainingdiary.fb_core

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ru.jrd_prime.trainingdiary.fb_core.config._CATEGORIES
import ru.jrd_prime.trainingdiary.fb_core.config._USERS
import ru.jrd_prime.trainingdiary.fb_core.models.Category
import ru.jrd_prime.trainingdiary.fb_core.models.User
import ru.jrd_prime.trainingdiary.impl.AppContainer

class FireBaseCore(appContainer: AppContainer) {
    private val db = appContainer.fireDB

    private val userRef = db.getReference(_USERS)


    fun addNewUserOnSignIn(uid: String?, name: String?, mail: String?) {
        val user = User(uid, name, mail)


        Log.d("TAG", "addNewUserOnSignIn: asd")


        if (uid != null) {
            userRef.child(uid).setValue(user)
        }

    }

    fun pushCategories() {
        val category = listOf<Category>(
            Category(1, "cardio"),
            Category(2, "power"),
            Category(3, "stretch"),
            Category(4, "rest")
        )
        for (cat in category) {
            db.getReference(_CATEGORIES).child(cat.id.toString()).setValue(cat.name)
        }

        val z = db.getReference(_CATEGORIES)
        z.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val TAG = "aadsdasd"
                val vaz = snapshot.value
                val z = vaz as List<*>
//TODO добавить категории в локальную бд для экономии траффика

                Log.d(TAG, "onDataChange: {${z[1]}}")
            }
        })
    }

}