package ru.jrd_prime.trainingdiary.fb_core

import android.util.Log
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
        for (cat in category) db.getReference(_CATEGORIES).setValue(cat, 10)
    }

}