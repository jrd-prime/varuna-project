package ru.jrd_prime.trainingdiary.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.model.PlaceStatisticModel

class DashboardViewModel : ViewModel() {
    var statTitle = "Statistics for the last month"
    val TAG = "dashVM"
    var place: PlaceStatisticModel? = null
    var workoutsSum = 0

    fun setPlaces(placeList: MutableList<PlaceStatisticModel>) {
    }

    /*  1 to "cardio",
        2 to "power",
        3 to "stretch",
        4 to "rest" */


}