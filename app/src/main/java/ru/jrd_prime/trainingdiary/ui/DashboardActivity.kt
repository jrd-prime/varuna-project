package ru.jrd_prime.trainingdiary.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.a_root_header.view.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.adapter.WorkoutPageAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.config.DATE_FORMAT_STRING
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.gauth.GAuth
import ru.jrd_prime.trainingdiary.handlers.GetWorkoutsCallback
import ru.jrd_prime.trainingdiary.handlers.pageListener
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.*
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
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
    private var gAuth: GAuth? = null
    private var navDrawerFragment: NavDrawerFragment? = null
    private val cfg: AppConfig = AppConfig()
    private var utils: AppUtils? = null
    internal val activity: Activity = this
    private var fireAuth: FirebaseAuth = Firebase.auth
    lateinit var fireBaseCore: FireBaseCore
    lateinit var appContainer: AppContainer
    val statisticAdapter = StatisticListAdapter()
    val DIALOG_EXIT = 1
    lateinit var binding: ActivityDashboardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContainer = (application as TrainingDiaryApp).container

        val database = appContainer.fireDB

        fireBaseCore = FireBaseCore(appContainer)
//fireBaseCore.addNewUserOnSignIn()

        // Add Categories
        fireBaseCore.pushCategories()
//
//        fireBaseCore.addMoreWorkout( "2020-07-20", Workout(id = "2020-07-20"))
//        fireBaseCore.addMoreWorkout( "2020-07-20", Workout(id = "2020-07-20"))
//        fireBaseCore.addMoreWorkout( "2020-07-20", Workout(id = "2020-07-20"))
        utils = appContainer.appUtils
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewmodel = dashboardViewModel
        val workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        val statisticRecyclerView: RecyclerView = findViewById<RecyclerView>(R.id.statListView)

        navDrawerFragment = NavDrawerFragment(appContainer)

/* START */
        val mSettings = getSharedPreferences(cfg.getSharedPreferenceName(), Context.MODE_PRIVATE)

        gAuth = appContainer.gAuth
/* END */
        binding.viewmodel = viewmodel
        setWindow()




        workoutPager.adapter = workoutPagerAdapter
        workoutPager.setCurrentItem(START_PAGE + 1, false)
        workoutPager.addOnPageChangeListener(pageListener)



        statisticAdapter.notifyDataSetChanged()
        statisticRecyclerView.adapter = statisticAdapter

        setSupportActionBar(vBottomAppBar)
/* START */
        if (mSettings.getBoolean(cfg.getSpNameFirstRun(), true)) {
            // Выполняем необходимые настройки для первого запуска
            utils!!.setDefaultConfig(mSettings, cfg)
        }

/* END */

        val date: MutableList<Long> = getWeekFromDate(getStartDateForPosition(START_PAGE))
        val statEndDate = date[1]
        val statStartDate = fromTimestamp(statEndDate).minusMonths(1)
//        val workoutsForMonth = appContainer.workoutsRepository.getWorkoutsForWeek(
//            dateToTimestamp(statStartDate),
//            statEndDate
//        )
//        workoutsForMonth.observe(
//            this,
//            Observer { list -> statisticAdapter.setNewData(viewmodel.setNewStatistic(list)) })

        val datez = getWeekFromDate(getStartDateForPosition(START_PAGE))
        val dates = getDatesMonthList(
            startDate = dateToTimestamp(LocalDateTime.now()),
            daysBack = 28
        )
        Log.d(TAG, "---------------------------------------------")
        Log.d(TAG, "onCreateView: $dates")
        Log.d(TAG, "---------------------------------------------")

        updateStat()

        fireBaseCore.listenNewData2(this)

        statisticRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    //todo проверить статистику на переходе месяцв
    fun updateStat() {
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

                statisticAdapter.setNewData(dashboardViewModel.setNewStatistic(dataList))
                setStats(dataList)
            }
        })
    }

    fun setStats(dataList: MutableList<Workout>) {
        var time = 0
        var cal = 0
        var dist = 0f

        for (workout in dataList) {
            time += workout.time
            cal += workout.kcal
            dist += workout.distance
        }

        val f = binding.frameHeader
        f.tvTime_Stat.text = resources.getString(R.string.minutes_val, time.toString())
        f.tvCalories_Stat.text = resources.getString(R.string.calories_val, cal.toString())
        f.tvDistance_Stat.text = resources.getString(R.string.distance_val, dist.toString())

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        if (fireAuth.currentUser != null) {
            Log.d(TAG, "onStart: USER NO NULL")
            val u = fireAuth.currentUser
//            if (u != null) {
////                 fireBaseCore.pushRan()
//            }
        }
        gAuth!!.getLastSignedInAccount()
    }

    //    override fun onResume() {
//        super.onResume()
//        navDrawerFragment = NavDrawerFragment()
//        navDrawerFragment!!.show(supportFragmentManager, navDrawerFragment!!.getTag())
//    }
    protected override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        result: Intent?
    ): Unit {
        super.onActivityResult(requestCode, resultCode, result)
        gAuth!!.onActivityResult(requestCode, resultCode, result)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottom_nav_drawer_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity: onResume()")

        /* Update Pager After Login */
        if (utils!!.getUserAuth()) {
            val workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
            workoutPager.adapter = workoutPagerAdapter
            workoutPager.setCurrentItem(START_PAGE, false)
            workoutPager.addOnPageChangeListener(pageListener)
            utils?.setShowMenu(false)
        }

//        if (utils!!.getShowMenu()) {
//            Log.d(TAG, "onResume: show menu")
//            navDrawerFragment!!.show(supportFragmentManager, navDrawerFragment!!.tag)
//            utils?.setShowMenu(false)
//        }


    }


    fun isAuthenticatedUser(mSettings: SharedPreferences): Boolean {
        return mSettings.getBoolean(cfg.getSpNameUserAuth(), false)
    }

    fun off() {
        if (gAuth!!.getGoogleSignInClient() != null) {
            gAuth!!.gSignOut()
        }
        Process.killProcess(Process.myPid())
    }

    private fun show_children(v: View) {
        val viewgroup = v as ViewGroup
        for (i in 0 until viewgroup.childCount) {
            val v1 = viewgroup.getChildAt(i)
            (v1 as? ViewGroup)?.let { show_children(it) }
            Log.d("APPNAME", v1.toString())
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ${item.itemId}")
        when (item.itemId) {
            android.R.id.home -> {
                navDrawerFragment!!.show(supportFragmentManager, navDrawerFragment!!.getTag())
                return true
            }
            1 -> return true
        }
        return true
    }

    override fun onBackPressed() {
        val root =
            (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup

        val yesListener = View.OnClickListener { _ ->
            Toast.makeText(
                this,
                "asd asdqwe123213 asd ",
                Toast.LENGTH_LONG
            ).show()
        }

        makeDialogYesOrNo(activity.applicationContext, root, yesListener, title = R.string.msg_exit_from_app)
        Log.d(TAG, "onBackPressed: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity: onPause()")
    }

    override fun onStop() {
        super.onStop()
        val clearCardVisibility = {
            val sh = applicationContext.getSharedPreferences(
                AppConfig.SHARED_PREFERENCE_NAME_FOR_CARD,
                Context.MODE_PRIVATE
            ).edit().clear().apply()
            Log.d(TAG, "ClearCardVisibility")
        }

        clearCardVisibility.invoke()
        Log.d(TAG, "MainActivity: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity: onDestroy()")
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