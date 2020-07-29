package ru.jrd_prime.trainingdiary.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.ads.AdLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.a_root_header.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.threeten.bp.LocalDateTime
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.StatisticListAdapter
import ru.jrd_prime.trainingdiary.adapter.WorkoutPageAdapter
import ru.jrd_prime.trainingdiary.databinding.ActivityDashboardBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.models.Premium
import ru.jrd_prime.trainingdiary.fb_core.models.User
import ru.jrd_prime.trainingdiary.gauth.GAuth
import ru.jrd_prime.trainingdiary.handlers.*
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.*
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
import ru.jrd_prime.trainingdiary.viewmodels.DashboardViewModel
import ru.jrd_prime.trainingdiary.viewmodels.StatisticViewModel


const val TAG = "Dashboard: drops:"
const val PAGE_COUNT = 5000
const val START_PAGE = 313

class DashboardActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }
    private val mPagerAdapter by lazy {
        WorkoutPageAdapter(supportFragmentManager)
    }
    private var mGoogleAuth: GAuth? = null
    private var mNavDrawerFragment: NavDrawerFragment? = null
    private val mConfig: AppConfig = AppConfig()
    private var mUtils: AppUtils? = null
    internal val activity: Activity = this
    private var fireAuth: FirebaseAuth = Firebase.auth
    lateinit var fbc: FireBaseCore
    lateinit var appCont: AppContainer
    lateinit var mStatView: RecyclerView
    private val mStatAdapter = StatisticListAdapter()
    lateinit var mStatViewModel: StatisticViewModel
    lateinit var mBinding: ActivityDashboardBinding
    lateinit var mPagerView: ViewPager
    lateinit var adLoader: AdLoader
    private var premiumStatus: Premium? = null
    var userStatus: User? = null
    var userID: String? = null

    companion object {
        const val PREMIUM_STATUS_FREE = "free"
        const val PREMIUM_STATUS_PRO = "premium"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* begin INIT */
        appCont = (application as TrainingDiaryApp).container
        mNavDrawerFragment = NavDrawerFragment(appCont)
        fbc = FireBaseCore(appCont)
        mGoogleAuth = appCont.gAuth
        mUtils = appCont.appUtils
        val mPref = getSharedPreferences(mConfig.getPrefName(), Context.MODE_PRIVATE)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        mBinding.viewmodel = mainViewModel
        /* end INIT */


        /* begin VIEWS */
//        mPagerView = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        mPagerView = mBinding.viewPagerMainDashboard
        mStatView = findViewById<RecyclerView>(R.id.statListView)


        /* end VIEWS */

        mStatViewModel = StatisticViewModel(this, mBinding)


        /* START */
        if (mPref.getBoolean(mConfig.getPrefNameFirstRun(), true)) {
            // Выполняем необходимые настройки для первого запуска
            mUtils!!.setDefaultConfig(mPref, mConfig)
        }
        /* END */

        userID = mGoogleAuth!!.getLastSignedInAccount()?.id

        Log.d(TAG, "- - - - - - - - - -")
        Log.d(TAG, "ON CREATE. Пытаемся опознать юзера и показать необходимый ЮИ")
        getUserInfo(userID)
        Log.d(TAG, "- - - - - - - - - -")


        //fireBaseCore.addNewUserOnSignIn()


        // Add Categories
        fbc.pushCategories()




        mStatView.adapter = mStatAdapter
        mStatAdapter.notifyDataSetChanged()
        mStatView.layoutManager = LinearLayoutManager(this)


//        var userID: String? = null
//        try {
//            userID = mGoogleAuth!!.getLastSignedInAccount()!!.id
//        } catch (e: NullPointerException) {
//        }
//        updateUIOnGetUserPremium(userID, appCont)


        setWindow()

        mPagerView.adapter = mPagerAdapter
        mPagerView.setCurrentItem(START_PAGE + 1, false)
        mPagerView.addOnPageChangeListener(pageListener)
        setSupportActionBar(vBottomAppBar)


        val date: MutableList<Long> = getWeekFromDate(getStartDateForPosition(START_PAGE))
        val statEndDate = date[1]
        val statStartDate = fromTimestamp(statEndDate).minusMonths(1)

        val datez = getWeekFromDate(getStartDateForPosition(START_PAGE))
        val dates = getDatesMonthList(
            startDate = dateToTimestamp(LocalDateTime.now()),
            daysBack = 28
        )

//        updateStat()
        fbc.listenNewData2(this)
    }

    private fun getUserInfo(userID: String?) {
        if (userID != null) {
            fbc.getUserInfo(object : UserInfo {
                override fun onChangeUserInfo(user: User?) {
                    if (user != null) {
                        when (user.auth) {
                            true -> Log.d(TAG, "Авторизация стоит в ТРУ")
                            false -> Log.d(TAG, "Авторизация стоит в ФОЛС")
                            null -> Log.d(TAG, "Еще нету записи об авторизации")
                        }
                    } else {
                        /* NO USER */
                        Log.d(TAG, "Юзера нету в БД")
                    }
                }
            }, userID)
        } else {
            Log.d(TAG, "Нету ИД пользователя, значит тут еще не логинились, или вышли из аккаунта")
            /* Инициализируем ЮИ для незалогиненного юзера*/
            mStatViewModel.showUnAuthUI(mStatAdapter) /* Статистика */
            mNavDrawerFragment?.showUnAuthUI()  /* Меню */
            WorkoutPageFragment().showUnAuthUI() /* Контент */
        }
    }

    fun updateUIOnGetUserPremium(
        userID: String?,
        appContainerz: AppContainer
    ) {

        if (!userID.isNullOrEmpty()) {
            FireBaseCore(appContainerz).getUserPremiumListener(object : UserPremiumChange {
                override fun onChangeUserPremium(premium: Premium?, uid: String) {
                    premiumStatus = premium
                    if (premium != null) {
                        when (premium.premium) {
                            true -> {
                                updateStatUI(PREMIUM_STATUS_PRO)
//                                updateStat()
                            }
                            false -> {
                                updateStatUI(PREMIUM_STATUS_FREE)
                            }
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Error on get user premium",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, userID)
        } else {
            Log.d(TAG, "onCreate: drops USER ID EMPTY!!")
//            updateStatUI(PREMIUM_STATUS_FREE)
            updateStatUI(PREMIUM_STATUS_PRO)
        }
    }

    private fun updateStatUI(status: String) {
        val statViewPremiumAd = statListViewPremiumAd
        when (status) {
            PREMIUM_STATUS_FREE -> {
                Log.d(TAG, "updateStatUI: $PREMIUM_STATUS_FREE")
                mStatAdapter.notifyDataSetChanged()
                setGone(mStatView)
                setVisbl(statViewPremiumAd)
            }

            PREMIUM_STATUS_PRO -> {
                Log.d(TAG, "updateStatUI: $PREMIUM_STATUS_PRO")
//                updateStat()
                mStatAdapter.notifyDataSetChanged()
                setVisbl(mStatView)
                setGone(statListViewPremiumAd)
            }
        }
    }

//    fun updateStat() {
//        StatisticViewModel().updateStat(
//            fbc,
//            statAdapter,
//            dashboardViewModel,
//            mainBinding
//        )
//    }

    override fun onStart() {
        super.onStart()
        if (fireAuth.currentUser != null) {
            Log.d(TAG, "onStart: FireAuth current user NOT NULL")
            val u = fireAuth.currentUser
        }
        mGoogleAuth!!.getLastSignedInAccount()
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
        mGoogleAuth!!.onActivityResult(requestCode, resultCode, result)
    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.bottom_nav_drawer_menu, menu)
//        return true
//    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity: onResume()")

        userID = appCont.gAuth.getLastSignedInAccount()?.id

        Log.d(TAG, "- - - - - - - - - -")
        Log.d(TAG, "ON RESUME. Пытаемся опознать юзера и показать необходимый ЮИ")
        getUserInfo(userID)
        Log.d(TAG, "- - - - - - - - - -")


        /* Update Pager After Login */
        if (mUtils!!.getUserAuth()) {
            val workoutPager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
            workoutPager.adapter = mPagerAdapter
            workoutPager.setCurrentItem(START_PAGE, false)
            workoutPager.addOnPageChangeListener(pageListener)
            mUtils?.setShowMenu(false)
            var userID: String? = null
            try {
                userID = mGoogleAuth!!.getLastSignedInAccount()!!.id
            } catch (e: NullPointerException) {
            }
            updateUIOnGetUserPremium(userID, appCont)
        }

//        if (utils!!.getShowMenu()) {
//            Log.d(TAG, "onResume: show menu")
//            navDrawerFragment!!.show(supportFragmentManager, navDrawerFragment!!.tag)
//            utils?.setShowMenu(false)
//        }


    }


    fun isAuthenticatedUser(mSettings: SharedPreferences): Boolean {
        return mSettings.getBoolean(mConfig.getSpNameUserAuth(), false)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ${item.itemId}")
        when (item.itemId) {
            android.R.id.home -> {
                mNavDrawerFragment!!.show(supportFragmentManager, mNavDrawerFragment!!.getTag())
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
            mUtils?.closeApp(this)
        }

        makeDialogYesOrNo(
            activity.applicationContext,
            root,
            yesListener,
            title = R.string.msg_exit_from_app
        )
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