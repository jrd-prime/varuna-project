package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.*
import ru.jrd_prime.trainingdiary.viewmodels.DashboardViewModel


const val TAG = "myLogs"
const val PAGE_COUNT = 5000
const val START_PAGE = 313

class DashboardActivity : AppCompatActivity() {
    private val dashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    private var workoutPager: ViewPager? = null
    private var workoutPagerAdapter: PagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as TrainingDiaryApp).container
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) -> {
//                // You can use the API that requires the permission.
//                Log.d(TAG, "onCreate: YES!")
//             //   BackUpDB().backUpDB() //TODO checkpermissions, then backup
//            }
//            else -> {
//                // You can directly ask for the permission.
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ),
//                    1
//                )
//            }
//        }


        val sha = getSharedPreferences("jrd", Context.MODE_PRIVATE)

        val binding: ActivityDashboardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewmodel = dashboardViewModel
        binding.viewmodel = viewmodel

        dashboardViewModel.statTitle = "new"

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


        val lvMain: RecyclerView = findViewById<RecyclerView>(R.id.statListView)
        var datata: List<WorkoutModel>? = null

        val myAdapter = StatisticListAdapter()
        myAdapter.notifyDataSetChanged()
        lvMain.adapter = myAdapter


        val date: MutableList<Long> =
            getWeekStartAndEndFromDate(getStartDateForPosition(START_PAGE))
        val dt1 = date[1]
        val dt2 = fromTimestamp(dt1).minusMonths(1)
        Log.d(
            TAG,
            "onCreate: ${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(fromTimestamp(dt1))}"
        )
        Log.d(TAG, "onCreate: ${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dt2)}")
        val data = appContainer.workoutsRepository.getWorkoutsForWeek(
            dateToTimestamp(dt2),
            dt1
        )


        data.observe(this, Observer { dataz ->
            Log.d(TAG, "onCreateView: ${dataz.size}")
            myAdapter.setNewData(viewmodel.setNewStatistic(dataz))
        })

        lvMain.layoutManager = LinearLayoutManager(this)
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            1 -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty()
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                ) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(
//                        this,
//                        "Permission denied to read your External storage",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                return
//            }
//        }
//    }

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