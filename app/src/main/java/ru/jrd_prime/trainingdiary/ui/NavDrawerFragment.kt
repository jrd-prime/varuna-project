package ru.jrd_prime.trainingdiary.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.lay_frg_navdrawer_not_auth.view.*
import kotlinx.android.synthetic.main.lay_frg_navdrawer_not_auth.view.ivUserAvatar
import kotlinx.android.synthetic.main.lay_frg_navdrawer_with_auth.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.models.User
import ru.jrd_prime.trainingdiary.handlers.RefreshCallback
import ru.jrd_prime.trainingdiary.handlers.setGone
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.AppSettingsCore
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig


class NavDrawerFragment(
    private val appContainer: AppContainer,
    private val UIWay: String,
    private val user: User?
) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "NavDrawerFrg: drops: "
    }

    lateinit var refreshCallback: RefreshCallback
    private var navigationView: NavigationView? = null
    private var btmShBeh: BottomSheetBehavior<*>? = null
    private var isUserAuth = false
    private var drawerLayout = 0
    private val mPref = appContainer.preferences
    private val mConfig = appContainer.appConfig
    private val mUtils = appContainer.appUtils
    private val mGoogleAuth = appContainer.gAuth
    private val authLayID = R.layout.lay_frg_navdrawer_with_auth
    private val notAuthLayID = R.layout.lay_frg_navdrawer_not_auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserAuth = if (user == null) false else user.auth!!
        when (UIWay) {
            UI_WAY_NO_USER -> {
                Log.d(TAG, "onCreate: $UI_WAY_NO_USER")
                drawerLayout = notAuthLayID
            }
            UI_WAY_USER -> {
                Log.d(TAG, "onCreate: $UI_WAY_USER")
                drawerLayout = authLayID
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(drawerLayout, container, false)
        Log.d(TAG, "onCreateView: USER ^ $isUserAuth")
        if (isUserAuth) {
            val premium = user?.premium ?: false
            if (premium) {
                user?.let { fillPremiumAuthUI(root, it) }
            } else {
                user?.let { fillAuthUI(root, it) }
            }
        } else {
            fillNotAuthUI(root)
            showNotAuthMenu(root)
        }

        navigationView = root.findViewById<NavigationView>(R.id.vNavigationView)
        setHasOptionsMenu(true)
        return root
    }

    private fun fillPremiumAuthUI(
        root: View,
        user: User
    ) {
        val res = root.resources
        val premium = user.premium ?: false

        Log.d(TAG, "fillAuthUI: premium = $premium")
        root.premiumBadge.visibility = View.VISIBLE
        root.ivUserAvatar.setImageDrawable(mUtils.getUserAvatar())
        root.tvUserName.text = mPref.getString(
            mConfig.getPrefUserName(),
            res.getString(R.string.empty_user_name)
        )
        root.tvUserMail.text = mPref.getString(
            mConfig.getPrefUserMail(),
            res.getString(R.string.empty_user_email)
        )
        root.ivLogOut.setOnClickListener { /*SIGN OUT*/
            mGoogleAuth.gSignOut(user.id.toString())

            dismiss()
//            refreshCallback.refreshActivity()
//            updatePagerOnLogOut()
        }

    }

    private fun showNotAuthMenu(root: View?) {
        /* HIDE MENU FOR NON-AUTH UI */
        setGone(root!!.findViewById<NavigationView>(R.id.vNavigationView))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RefreshCallback) {
            refreshCallback = context as RefreshCallback
        } else {
            throw RuntimeException(
                context.javaClass.simpleName
                        + " must implement Callback"
            )
        }
    }

    private fun fillAuthUI(
        root: View,
        user: User
    ) {
        val res = root.resources
        val premium = user.premium ?: false
        root.premiumBadgeNo.visibility = View.VISIBLE

        Log.d(TAG, "fillAuthUI: premium = $premium")


        root.ivUserAvatar.setImageDrawable(mUtils.getUserAvatar())
        root.tvUserName.text = mPref.getString(
            mConfig.getPrefUserName(),
            res.getString(R.string.empty_user_name)
        )
        root.tvUserMail.text = mPref.getString(
            mConfig.getPrefUserMail(),
            res.getString(R.string.empty_user_email)
        )
        root.ivLogOut.setOnClickListener { /*SIGN OUT*/
            mGoogleAuth.gSignOut(user.id.toString())

            dismiss()
//            refreshCallback.refreshActivity()
//            updatePagerOnLogOut()
        }

    }

    private fun fillNotAuthUI(root: View) {
        val res = root.resources
        root.tvUserNameNA.text = res.getString(R.string.empty_user_name)
        root.tvUserMailNA.text = res.getString(R.string.empty_user_email)

        root.bSignIn.setOnClickListener {
            Log.d(TAG, "SIGN IN")
            mGoogleAuth.gSignIn(activity)
//            refreshCallback.refreshActivity()
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

                override fun onSlide(view: View, slideOffset: Float) {
//                    if (slideOffset > 0.5) closeImage.setVisibility(View.VISIBLE) else closeImage.setVisibility(
//                        View.GONE
//                    )
                }
            })
        }

        // Do something with your dialog like setContentView() or whatever
        return dialog
    }
}