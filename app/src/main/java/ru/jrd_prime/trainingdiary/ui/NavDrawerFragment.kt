package ru.jrd_prime.trainingdiary.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.marginBottom
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
import ru.jrd_prime.trainingdiary.impl.AppContainer


class NavDrawerFragment(appContainer: AppContainer) : BottomSheetDialogFragment() {
    private var navigationView: NavigationView? = null
    private var btmShBeh: BottomSheetBehavior<*>? = null
    private var isUserAuth = false
    private var mainLayout = 0
    private val shPref = appContainer.sharedPreferences
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
        if (isUserAuth) {
            // AUTH
            // Update UI
            root.ivUserAvatar.setImageDrawable(utils.getUserAvatar())
            root.tvUserName.text = shPref.getString(cfg.getSpNameUserName(), "Err")
            root.tvUserMail.text = shPref.getString(cfg.getSpNameUserMail(), "Err")
            root.ivLogOut.setOnClickListener { /*SIGN OUT*/
                gAuth.gSignOut()
                dismiss()
                show(requireActivity().supportFragmentManager, "asd")
            }
        } else {
            // NOT AUTH
            // Update UI
            shPref.edit().putBoolean(cfg.getSpNameUserAuth(), false).apply()
            // FIND VIEWS IN NOT AUTH NAV
            root.bSignIn.setOnClickListener {
                Log.d("TAG", "SIGN IN")
                /*SIGN IN*/
                gAuth.gSignIn(activity)
                dismiss()
            }
        }
        navigationView = root.findViewById(R.id.vNavigationView)
        // closeImage = root.findViewById(R.id.close_imageview);
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Snackbar.make(navigationView!!, "dada", Snackbar.LENGTH_SHORT)
        navigationView!!.setNavigationItemSelectedListener { menuItem ->
            menuItem.itemId
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