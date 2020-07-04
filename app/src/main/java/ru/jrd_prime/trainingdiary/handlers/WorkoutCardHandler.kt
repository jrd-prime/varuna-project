package ru.jrd_prime.trainingdiary.handlers

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.Toast
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.pop.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp


class WorkoutCardHandler(root: View) {
    val appContainer = (root.context.applicationContext as TrainingDiaryApp).container


    fun workoutDelete(view: View, workoutID: Int) {
        Toast.makeText(view.context, "Del $workoutID", Toast.LENGTH_SHORT).show()

        runBlocking {
            launch(Dispatchers.IO) {
//                WorkoutDatabase.getInstance(view.context).workoutDao().delete(workoutID)
                appContainer.workoutsRepository.clearWorkout(workoutID, true)
            }
        }
        Snackbar.make(view, "asdasd", Snackbar.LENGTH_LONG)
            .setAction("Cancel", View.OnClickListener { v ->
                runBlocking {
                    launch(Dispatchers.IO) {
//                WorkoutDatabase.getInstance(view.context).workoutDao().delete(workoutID)
                        appContainer.workoutsRepository.restoreWorkout(workoutID, false)
                    }
                }
            }).show()
    }

    fun workoutEdit(view: View, workoutID: Int) {
        Toast.makeText(view.context, "Edit $workoutID", Toast.LENGTH_SHORT).show()
    }

    fun workoutAdd(view: View, workoutID: Int) {
        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop, null)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )

        // main container
        val popupContainer = popupView.puMainContainer
        // layouts
        val categoryRadio = popupContainer.categoryRadio// radio container
        val layGrpAndTime = popupContainer.layoutGroupsAndTime // group and time container
        val layDesc = popupContainer.layoutDescription // desc container
        // radio btns
        val rRest = popupContainer.btnRest
        val rStretch = popupContainer.btnStretch
        val rPower = popupContainer.btnPower
        val rCardio = popupContainer.btnCardio
        // edit fields
        val etGroup = popupContainer.etGroups
        val etMins = popupContainer.etMins
        val etDesc = popupContainer.etDescription
        // btns
        val btnCancel = popupContainer.btnCancel
        val btnSave = popupContainer.btnSave

        btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        // DEF SET
        layGrpAndTime.visibility = View.VISIBLE
        popupContainer.viewForHide.visibility = View.VISIBLE

        /* Обработка радио*/
        // set defaults
        rRest.isChecked = false
        rStretch.isChecked = false
        rPower.isChecked = false
        rCardio.isChecked = false

        val rbListener = View.OnClickListener { btn ->
            when (btn.id) {
                rRest.id -> {
                    rRest.isChecked = true
                    rRest.setColoredBg()
                    rStretch.setTransBg()
                    rPower.setTransBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.GONE
                    popupContainer.viewForHide.visibility = View.GONE
                    Log.d("TAG", "workoutAdd: ${rRest.isChecked}")
                }
                rStretch.id -> {
                    rStretch.isChecked = true
                    rRest.setTransBg()
                    rStretch.setColoredBg()
                    rPower.setTransBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    Log.d("TAG", "workoutAdd: ${rStretch.isChecked}")
                }
                rPower.id -> {
                    rPower.isChecked = true
                    rRest.setTransBg()
                    rStretch.setTransBg()
                    rPower.setColoredBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    Log.d("TAG", "workoutAdd: ${rPower.isChecked}")
                }
                rCardio.id -> {
                    rCardio.isChecked = true
                    rRest.setTransBg()
                    rStretch.setTransBg()
                    rPower.setTransBg()
                    rCardio.setColoredBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    Log.d("TAG", "workoutAdd: ${rCardio.isChecked}")
                }
            }
        }

        rRest.setOnClickListener(rbListener)
        rStretch.setOnClickListener(rbListener)
        rPower.setOnClickListener(rbListener)
        rCardio.setOnClickListener(rbListener)



        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private fun RadioButton.setTransBg() {
        setBackgroundResource(R.drawable.jp_radio_bg_transparent)
    }

    private fun RadioButton.setColoredBg() {
        setBackgroundResource(R.drawable.jp_radio_bg)
    }

    fun onGo(view: View?) {
        if (view == null) return
        val contView = view.findViewById<LinearLayout>(R.id.frameForHide)

        when (contView.visibility) {
            View.GONE -> setVisible(contView)
            View.VISIBLE -> setGone(contView)
        }

//
//        contView?.animate()
//            ?.translationY(contView.height.toFloat())
//            ?.alpha(1.0f)?.duration = 500
//            ?.setListener(object : AnimatorListenerAdapter() {
//
//                override fun onAnimationEnd(animation: Animator) {
//                    super.onAnimationEnd(animation)
//                    contView.visibility = View.VISIBLE
//                }
//            })

    }

    private fun setGone(contView: LinearLayout) {
        TransitionManager.beginDelayedTransition(contView, AutoTransition())
        contView.visibility = View.GONE
    }

    private fun setVisible(contView: LinearLayout) {
        TransitionManager.beginDelayedTransition(contView, AutoTransition())
        contView.visibility = View.VISIBLE
    }
}