package ru.jrd_prime.trainingdiary.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.model.PlaceStatisticModel

class DashboardViewModel : ViewModel() {
    var statTitle = "Statistics for the last month"
    val TAG = "dashVM"
    var place: PlaceStatisticModel? = null
    var workoutsSum = 0
    var p1icon: Int = R.drawable.jp_day_fri
    var p2icon: Int = R.drawable.jp_day_fri
    var p3icon: Int = R.drawable.jp_day_fri
    var p4icon: Int = R.drawable.jp_day_fri

    var p1progress = 0
    var p2progress = 0
    var p3progress = 0
    var p4progress = 0

    fun setPlaces(placeList: MutableList<PlaceStatisticModel>) {
    }

    /*  1 to "cardio",
        2 to "power",
        3 to "stretch",
        4 to "rest" */

    fun setNewStatistic(list: List<Workout>): List<PlaceStatisticModel> {
//        val data = list.filter { workoutModel -> !workoutModel.empty }
        val data = list
        val cardioSize = data.filter { workoutModel -> workoutModel.category == 1 }.size
        val powerSize = data.filter { workoutModel -> workoutModel.category == 2 }.size
        val stretchSize = data.filter { workoutModel -> workoutModel.category == 3 }.size
        val restSize = data.filter { workoutModel -> workoutModel.category == 4 }.size

        workoutsSum = cardioSize + powerSize + stretchSize + restSize


        val onePercent: Float =
            if (workoutsSum != 0) 100f / workoutsSum else 0f // no records - set def

        val cardioPercent = onePercent * cardioSize
        val powerPercent = onePercent * powerSize
        val stretchPercent = onePercent * stretchSize
        val restPercent = onePercent * restSize

        val z = mutableListOf<PlaceStatisticModel>()
        z.add(PlaceStatisticModel(1, cardioPercent))
        z.add(PlaceStatisticModel(2, powerPercent))
        z.add(PlaceStatisticModel(3, stretchPercent))
        z.add(PlaceStatisticModel(4, restPercent))
        var maxPercent = 0f
        for (item in z) {
            maxPercent = if (item.catPercent > maxPercent) item.catPercent else maxPercent
        }
        z.sortByDescending { it.catPercent }
        val placed = listOf<PlaceStatisticModel>(z[0], z[1], z[2], z[3])
        placed[0].catPlace = 1
        placed[1].catPlace = 2
        placed[2].catPlace = 3
        placed[3].catPlace = 4

        Log.d(
            TAG,
            "setNewStatistics\nCardio: $cardioSize \nPower: $powerSize \nStretch: $stretchSize \nRest: $restSize"
        )
        return placed
    }
}