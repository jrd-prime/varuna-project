package ru.jrd_prime.trainingdiary.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.impl.AppContainer


class NavDrawerFragment(private val appContainer: AppContainer) : BottomSheetDialogFragment() {
    private var navigationView: NavigationView? = null
    private var btmShBeh: BottomSheetBehavior<*>? = null
    private var isUserAuth = false
    var containerX: ViewGroup? = null
    private var mainLayout = 0
    private val shPref = appContainer.sharedPreferences
    private val cfg = appContainer.appConfig
    private val utils = appContainer.appUtils
    private val gAuth = appContainer.gAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserAuth = shPref.getBoolean(cfg.getSpNameUserAuth(), false)
        mainLayout = if (isUserAuth) {
            // AUTH
            R.layout.lay_frg_navdrawer_with_auth
        } else {
            // NOT AUTH
            R.layout.lay_frg_navdrawer_not_auth
        }
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
            val ivUserPhoto =
                root.findViewById<ImageView>(R.id.ivUserAvatar)
            val tvUserName = root.findViewById<TextView>(R.id.tvUserName)
            val tvUserMail = root.findViewById<TextView>(R.id.tvUserMail)
            val bLogOut =
                root.findViewById<ImageView>(R.id.ivLogOut)

            ivUserPhoto.setImageDrawable(utils.getUserAvatar())
            tvUserName.setText(shPref.getString(cfg.getSpNameUserName(), "Err"))
            tvUserMail.setText(shPref.getString(cfg.getSpNameUserMail(), "Err"))
            bLogOut.setOnClickListener { //TODO SIGN OUT
                gAuth.GSignOut()
                dismiss()
                show(requireActivity().supportFragmentManager, "asd")
            }
        } else {
            // NOT AUTH
            // Update UI
            shPref.edit().putBoolean(cfg.getSpNameUserAuth(), false).apply()
            // FIND VIEWS IN NOT AUTH NAV
            val bLogIn = root.findViewById<View>(R.id.bSignIn)
            bLogIn.setOnClickListener {
                Log.d("TAG", "SIGN IN")
                //TODO SIGN IN
                gAuth.GSignIn(activity)
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
        val dialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            btmShBeh = BottomSheetBehavior.from(bottomSheet)

            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED;
            (btmShBeh as BottomSheetBehavior<FrameLayout?>?)?.setBottomSheetCallback(object :
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