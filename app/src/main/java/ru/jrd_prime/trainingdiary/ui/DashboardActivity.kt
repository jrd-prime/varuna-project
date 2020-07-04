package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.data.initDB
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.utils.makeStatusBarTransparent
import java.text.SimpleDateFormat
import java.util.*


const val TAG = "myLogs"
const val PAGE_COUNT = 5000
const val START_PAGE = 1500

class DashboardActivity : AppCompatActivity() {
    private val dashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    var workoutPager: ViewPager? = null
    var workoutPagerAdapter: PagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as TrainingDiaryApp).container

        val sha = getSharedPreferences("jrd", Context.MODE_PRIVATE)

//        if (sha.getInt("need_db", 1) == 1) {
//            Log.d("DASHBOARD_ACTIVITY", "DB INIT")
//            initDB(this)
//            sha.edit().putInt("need_db", 0).apply()
//        }

        val binding: ActivityDashboardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewmodel = dashboardViewModel
        binding.viewmodel = viewmodel


        setWindow()

        workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        workoutPagerAdapter = WorkoutPageAdapter(supportFragmentManager)

        workoutPager!!.adapter = workoutPagerAdapter

        workoutPager!!.setCurrentItem(START_PAGE, false)

        workoutPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                Log.d(TAG, "onPageSelected, position = $position")
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    private class WorkoutPageAdapter(
        fm: FragmentManager?
    ) : FragmentPagerAdapter(fm!!) {


        override fun getItem(position: Int): Fragment {
            return WorkoutPageFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return PAGE_COUNT
        }
    }

    private fun setWindow() {
        makeStatusBarTransparent()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coordinatorAppContainer)) { _, insets ->
//            findViewById<FloatingActionButton>(R.id.vFloatActionBar).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.coordinatorAppContainer)
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