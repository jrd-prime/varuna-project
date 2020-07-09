package ru.jrd_prime.trainingdiary.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.adapter.WorkoutPageAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.handlers.pageListener
import ru.jrd_prime.trainingdiary.utils.*
import ru.jrd_prime.trainingdiary.viewmodels.DashboardViewModel


const val TAG = "myLogs"
const val PAGE_COUNT = 5000
const val START_PAGE = 313

class DashboardActivity : AppCompatActivity() {
    private val dashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    private val workoutPagerAdapter by lazy {
        WorkoutPageAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as TrainingDiaryApp).container
        val binding: ActivityDashboardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewmodel = dashboardViewModel
        val workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        val statisticRecyclerView: RecyclerView = findViewById<RecyclerView>(R.id.statListView)
        val statisticAdapter = StatisticListAdapter()

        binding.viewmodel = viewmodel
        setWindow()
        workoutPager.adapter = workoutPagerAdapter
        workoutPager.setCurrentItem(START_PAGE, false)
        workoutPager.addOnPageChangeListener(pageListener)
        statisticAdapter.notifyDataSetChanged()
        statisticRecyclerView.adapter = statisticAdapter

        val date: MutableList<Long> = getWeekFromDate(getStartDateForPosition(START_PAGE))
        val statEndDate = date[1]
        val statStartDate = fromTimestamp(statEndDate).minusMonths(1)
        val workoutsForMonth = appContainer.workoutsRepository.getWorkoutsForWeek(
            dateToTimestamp(statStartDate),
            statEndDate
        )
        workoutsForMonth.observe(
            this,
            Observer { list -> statisticAdapter.setNewData(viewmodel.setNewStatistic(list)) })

        statisticRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setWindow() {
        makeStatusBarTransparent()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordAppLayer)) { _, insets ->
            insets.consumeSystemWindowInsets()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.coordAppLayer)
        ) { _, insets ->
//            val menu = findViewById<FloatingActionButton>(R.id.vFloatActionBar)
//            val menuLayoutParams = menu.layoutParams as ViewGroup.MarginLayoutParams
//            menuLayoutParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
//            menu.layoutParams = menuLayoutParams
            insets.consumeSystemWindowInsets()
        }

        window.navigationBarColor = resources.getColor(
            R.color.colorPrimaryDark,
            applicationContext.theme
        )
    }
}