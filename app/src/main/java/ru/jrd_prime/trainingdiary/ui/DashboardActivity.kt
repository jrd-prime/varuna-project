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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dashboard.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.adapter.WorkoutPageAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.gauth.GAuth
import ru.jrd_prime.trainingdiary.handlers.pageListener
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.ui.dialog.ExitDialog
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

    val DIALOG_EXIT = 1
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
        val binding: ActivityDashboardBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        val viewmodel = dashboardViewModel
        val workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        val statisticRecyclerView: RecyclerView = findViewById<RecyclerView>(R.id.statListView)
        val statisticAdapter = StatisticListAdapter()
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

        statisticRecyclerView.layoutManager = LinearLayoutManager(this)


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
        val dl = ExitDialog()
        dl.show(supportFragmentManager, "exitDialog")
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