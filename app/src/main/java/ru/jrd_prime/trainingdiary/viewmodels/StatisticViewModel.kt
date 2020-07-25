package ru.jrd_prime.trainingdiary.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.a_root_header.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.config.DATE_FORMAT_STRING
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.handlers.GetWorkoutsCallback
import ru.jrd_prime.trainingdiary.ui.TAG

class StatisticViewModel : ViewModel() {
    //todo проверить статистику на переходе месяцв
    fun updateStat(
        fireBaseCore: FireBaseCore,
        statAdapter: StatisticListAdapter,
        mainViewModel: DashboardViewModel,
        binding: ActivityDashboardBinding
    ) {
        fireBaseCore.getData(object : GetWorkoutsCallback {
            override fun onWorkoutsCallBack(workouts: DataSnapshot) {
                val workoutsList: Iterable<DataSnapshot> = workouts.children
                val dataList = mutableListOf<Workout>()
                val last =
                    DateTimeFormatter.ofPattern(DATE_FORMAT_STRING).format(LocalDateTime.now())
                val cutter = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING)
                    .format(LocalDateTime.now().plusDays(1))
                Log.d(TAG, "onWorkoutsCallBack: $last")
                for (d in workoutsList) {
                    val workout = d.getValue<Workout>()
                    if (workout != null) {

                        if (workout.id == cutter) {
                            Log.d(TAG, "CUTTER: $cutter")
                            break
                        }

                        if (workout.empty && workout.category != 4) {
                            fireBaseCore.deleteMainWorkout(workout.id)
                        }
                        if (!workout.empty) {
                            dataList.add(workout)
                        } else if (workout.category == 4) {
                            dataList.add(workout)
                        }

                        val adds = workout.additional
                        if (adds != null) {
                            for (ad in adds) {
                                dataList.add(ad.value)
                            }
                        }
                    }
                }

                statAdapter.setNewData(mainViewModel.setNewStatistic(dataList))
                setStats(dataList, binding)
            }
        })
    }

    fun setStats(
        dataList: MutableList<Workout>,
        binding: ActivityDashboardBinding
    ) {
        val statRootView = binding.frameHeader
        val res = binding.root.context.resources
        var time = 0
        var cal = 0
        var dist = 0f

        for (workout in dataList) {
            time += workout.time
            cal += workout.kcal
            dist += workout.distance
        }

        statRootView.tvTime_Stat.text = res.getString(R.string.minutes_val, time.toString())
        statRootView.tvCalories_Stat.text = res.getString(R.string.calories_val, cal.toString())
        statRootView.tvDistance_Stat.text = res.getString(R.string.distance_val, dist.toString())
    }
}