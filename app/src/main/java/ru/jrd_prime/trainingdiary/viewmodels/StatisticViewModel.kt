package ru.jrd_prime.trainingdiary.viewmodels

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.a_root_header.view.*
import kotlinx.android.synthetic.main.stat_example_ui.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.config.DATE_FORMAT_STRING
import ru.jrd_prime.trainingdiary.fb_core.models.User
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.handlers.GetWorkoutsCallback
import ru.jrd_prime.trainingdiary.model.PlaceStatisticModel

class StatisticViewModel(private val ctx: Context, private val mBinding: ActivityDashboardBinding) :
    ViewModel() {
    var statTitle = "Statistics for the last month"
    val TAG = "dashVM"
    var place: PlaceStatisticModel? = null
    var workoutsSum = 0
    val statContainer = mBinding.frameHeader.layHeader
    val statView = statContainer.statListView
    val li: LayoutInflater =
        statContainer.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //todo проверить статистику на переходе месяцв
    fun updateStat(
        fbc: FireBaseCore,
        statAdapter: StatisticListAdapter,
        mainViewModel: DashboardViewModel,
        binding: ActivityDashboardBinding
    ) {
        fbc.getData(object : GetWorkoutsCallback {
            override fun onWorkoutsCallBack(workouts: DataSnapshot) {
                val workoutsList: Iterable<DataSnapshot> = workouts.children
                val dataList = mutableListOf<Workout>()
                val last =
                    DateTimeFormatter.ofPattern(DATE_FORMAT_STRING).format(LocalDateTime.now())
                val cutter = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING)
                    .format(LocalDateTime.now().plusDays(1))
                for (d in workoutsList) {
                    val workout = d.getValue<Workout>()
                    if (workout != null) {
                        if (workout.id == cutter) break

                        if (workout.empty && workout.category != 4) fbc.deleteMainWorkout(workout.id)

                        if (!workout.empty) {
                            dataList.add(workout)
                        } else if (workout.category == 4) {
                            dataList.add(workout)
                        }
                        val adds = workout.additional
                        if (adds != null) for (ad in adds) dataList.add(ad.value)
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

//        statRootView.tvTime_Stat.text = res.getString(R.string.minutes_val, time.toString())
        statContainer.tvTime_Stat.text = Workout(time = time).convertMinsToHM(res)
        statContainer.tvCalories_Stat.text = res.getString(R.string.calories_val, cal.toString())
        statContainer.tvDistance_Stat.text = res.getString(R.string.distance_val, dist.toString())
    }

    fun calculateStatistic(list: List<Workout>): List<PlaceStatisticModel> {
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

    fun showUnAuthorizedStat() {
        val adapt = StatisticListAdapter()

        statContainer.removeAllViews()
        val exampleView = li.inflate(R.layout.stat_example_ui, null)
        statContainer.addView(exampleView)
        val statView = statContainer.findViewById<RecyclerView>(R.id.statListView)
        val res = statContainer.context.resources

        statView.adapter = adapt
        statContainer.tvTime_Stat.text = Workout(time = 313).convertMinsToHM(res)
        statContainer.tvCalories_Stat.text = res.getString(R.string.calories_val, 2020.toString())
        statContainer.tvDistance_Stat.text = res.getString(R.string.distance_val, 38.5f.toString())
        statView.layoutManager = LinearLayoutManager(statContainer.context)
        val previewData = mutableListOf<PlaceStatisticModel>()
        previewData.add(PlaceStatisticModel(1, 43f, 1))
        previewData.add(PlaceStatisticModel(2, 27f, 2))
        previewData.add(PlaceStatisticModel(3, 16.3f, 3))
        previewData.add(PlaceStatisticModel(4, 13.7f, 4))
        adapt.setNewData(previewData)
    }

    fun showAuthorizedStat(
        premiumStatus: Boolean,
        user: User
    ) {
        when (premiumStatus) {
            true -> showPremiumUI()
            false -> showFreeUI()
        }
    }

    private fun showFreeUI() {
        val adapt = StatisticListAdapter()

        statContainer.removeAllViews()
        val freeView = li.inflate(R.layout.stat_free_ui, null)
        statContainer.addView(freeView)

    }

    private fun showPremiumUI() {
        val adapt = StatisticListAdapter()

        statContainer.removeAllViews()
        val exampleView = li.inflate(R.layout.stat_premium_ui, null)
        statContainer.addView(exampleView)
        val statView = statContainer.findViewById<RecyclerView>(R.id.statListView)
        val res = statContainer.context.resources

        statView.adapter = adapt
        statContainer.tvTime_Stat.text = Workout(time = 313).convertMinsToHM(res)
        statContainer.tvCalories_Stat.text = res.getString(R.string.calories_val, 2020.toString())
        statContainer.tvDistance_Stat.text = res.getString(R.string.distance_val, 38.5f.toString())
        statView.layoutManager = LinearLayoutManager(statContainer.context)
    }
}