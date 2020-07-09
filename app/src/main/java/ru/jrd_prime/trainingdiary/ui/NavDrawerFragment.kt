package ru.jrd_prime.trainingdiary.ui

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import ru.jrd_prime.trainingdiary.R


class NavDrawerFragment : BottomSheetDialogFragment() {
    private var navigationView: NavigationView? = null
    private var btmShBeh: BottomSheetBehavior<*>? = null
    private var isUserAuth = false
    var containerX: ViewGroup? = null
    private var mainLayout = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.lay_frg_navdrawer_not_auth, container, false)
        navigationView = root.findViewById(R.id.vNavigationView)
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

                            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            (btmShBeh as BottomSheetBehavior<FrameLayout?>?)?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

