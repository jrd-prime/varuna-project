package ru.jrd_prime.trainingdiary.db_core

import android.util.Log
import ru.jrd_prime.trainingdiary.fb_core.models.Category

class LocalDataBase {
    private val TAG: String? = "LocalDataBase"

    fun insertCategories(catList: List<Category>) {
        Log.d(TAG, "insertCategories: $catList")
    }

}