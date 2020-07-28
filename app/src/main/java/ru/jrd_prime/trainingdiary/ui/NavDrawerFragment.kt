package ru.jrd_prime.trainingdiary.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.lay_frg_navdrawer_not_auth.view.*
import kotlinx.android.synthetic.main.lay_frg_navdrawer_not_auth.view.ivUserAvatar
import kotlinx.android.synthetic.main.lay_frg_navdrawer_with_auth.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.adapter.WorkoutPageAdapter
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.handlers.pageListener
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.AppSettingsCore
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig


class NavDrawerFragment(private val appContainer: AppContainer) : BottomSheetDialogFragment() {
    private var navigationView: NavigationView? = null
    private var btmShBeh: BottomSheetBehavior<*>? = null
    private var isUserAuth = false
    private var mainLayout = 0
    private val shPref = appContainer.preferences
    private val cfg = appContainer.appConfig
    private val utils = appContainer.appUtils
    private val gAuth = appContainer.gAuth
    private val authLayID = R.layout.lay_frg_navdrawer_with_auth
    private val notAuthLayID = R.layout.lay_frg_navdrawer_not_auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserAuth = shPref.getBoolean(cfg.getSpNameUserAuth(), false)
        mainLayout = if (isUserAuth) authLayID else notAuthLayID
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(mainLayout, container, false)

//        val me = root.findViewById<NavigationView>(R.id.vNavigationView).menu
//        val me2 = me.findItem(R.id.navCloseApp)

//        me2.setTitle("fdpsfkqwef")
//        Log.d(TAG, "onCreateView: $me2")

        if (isUserAuth) {
            // AUTH
            // Update UI
            root.ivUserAvatar.setImageDrawable(utils.getUserAvatar())
            root.tvUserName.text = shPref.getString(cfg.getSpNameUserName(), "Err")
            root.tvUserMail.text = shPref.getString(cfg.getSpNameUserMail(), "Err")
            root.ivLogOut.setOnClickListener { /*SIGN OUT*/
                gAuth.gSignOut()
                dismiss()
                updatePagerOnLogOut()
//                show(requireActivity().supportFragmentManager, "asd")
            }
        } else {
            // NOT AUTH
            // Update UI
            shPref.edit().putBoolean(cfg.getSpNameUserAuth(), false).apply()
            // FIND VIEWS IN NOT AUTH NAV
            root.bSignIn.setOnClickListener {
                Log.d("TAG", "SIGN IN")
                gAuth.gSignIn(activity)
                dismiss()
            }
        }

        navigationView = root.findViewById<NavigationView>(R.id.vNavigationView)
        setHasOptionsMenu(true)


        // closeImage = root.findViewById(R.id.close_imageview);
        return root
    }

    private fun updatePagerOnLogOut() {
        Log.d(ru.jrd_prime.trainingdiary.ui.TAG, "onResume: update on logout")

        val workoutPager = activity?.findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        workoutPager?.adapter = activity?.supportFragmentManager?.let { WorkoutPageAdapter(it) }
        workoutPager?.setCurrentItem(START_PAGE, false)
        workoutPager?.addOnPageChangeListener(pageListener)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Snackbar.make(navigationView!!, "dada", Snackbar.LENGTH_SHORT)

        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.navCloseApp -> {
//                    WorkoutListAdapter().changed2(context)
//                }
                R.id.premiumOn -> {
                    FireBaseCore(appContainer).setPremium(true)
                }
                R.id.premiumOff -> {
                    FireBaseCore(appContainer).setPremium(false)

                }
                R.id.changeSet -> {
                    val pref = context?.getSharedPreferences(
                        AppConfig.SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE
                    )

                    if (pref != null) {
                        if (!pref.getBoolean("show_work", false)) {
                            context?.let { AppSettingsCore(it).setTru() }
                        } else context?.let {
                            AppSettingsCore(it).setFalse()
                        }
                    }

                    val int = Intent(context, DashboardActivity::class.java)
                    startActivity(int)
                    activity?.finish()


                }
            }
            true
        }
        disableNavigationViewScrollbars(navigationView)
    }

    private fun disableNavigationViewScrollbars(navigationView: NavigationView?) {
        val navigationMenuView =
            navigationView!!.getChildAt(0) as NavigationMenuView
        navigationMenuView.isVerticalScrollBarEnabled = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog



        dialog.setOnShowListener { bsdialog ->
            val d = bsdialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.setBackgroundColor(context?.resources?.getColor(R.color.jpPrimary2)!!)
            btmShBeh = BottomSheetBehavior.from(bottomSheet)
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
//            (btmShBeh as BottomSheetBehavior<FrameLayout?>?)?.setBottomSheetCallback(object : // OLD LINE
            btmShBeh?.setBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(
                    view: View,
                    newState: Int
                ) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> dismiss()
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                        }
                    }
                }

                override fun onSlide(
                    view: View,
                    slideOffset: Float
                ) {
                    //                        if (slideOffset > 0.5) {
                    //                            closeImage.setVisibility(View.VISIBLE);
                    //                        } else {
                    //                            closeImage.setVisibility(View.GONE);
                    //                        }
                }
            })
        }

        // Do something with your dialog like setContentView() or whatever
        return dialog
    }
}