package ru.jrd_prime.trainingdiary.handlers

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.pop.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.model.WorkoutModel


class WorkoutCardHandler(root: View) {
    private val appContainer = (root.context.applicationContext as TrainingDiaryApp).container


    fun workoutDelete(view: View, workoutID: Int) {
        val ctx: Context = view.context
        clearWorkout(workoutID) /* Set workout empty = true */
        val snack = Snackbar.make(view, R.string.snack_record_deleted, 25000)
        val snackView = snack.view
        snack.setAction(R.string.snack_restore, View.OnClickListener { _ ->
            restoreWorkout(workoutID) /* Set workout empty = false */
        })
        snack.setActionTextColor(ctx.getColor(R.color.colorSnackbarButton))
        snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ctx.getColor(R.color.colorLightGrey))
        snackView.setBackgroundResource(R.drawable.snack_bg)
        snack.show()
    }

    private fun restoreWorkout(workoutID: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                appContainer.workoutsRepository.restoreWorkout(workoutID, false)
            }
        }
    }

    private fun clearWorkout(workoutID: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                appContainer.workoutsRepository.clearWorkout(workoutID, true)
            }
        }
    }

    fun workoutEdit(view: View, workoutID: Int) {
        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop, null)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        var wo: WorkoutModel? = null
        runBlocking {
            launch(Dispatchers.IO) {
                wo = appContainer.workoutsRepository.getWorkout(workoutID)
            }
        }

        if (wo == null) {
            Toast.makeText(view.context, "Error on get workout ID: $workoutID", Toast.LENGTH_SHORT)
                .show()
        } else {

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

            // DEF SET
            layGrpAndTime.visibility = View.VISIBLE
            popupContainer.viewForHide.visibility = View.VISIBLE
            btnSave.isEnabled = false


            /* Обработка радио*/
            // set defaults
            rRest.isChecked = false
            rStretch.isChecked = false
            rPower.isChecked = false
            rCardio.isChecked = false
            when (wo!!.workoutCategory) {
                0 -> ""
                1 -> {
                    rCardio.isChecked = true
                    etGroup.setText(wo!!.muscleGroup.toString())
                    etDesc.setText(wo!!.desc.toString())
                    etMins.setText("" + wo!!.workoutTime)
                    rRest.setTransBg()
                    rStretch.setTransBg()
                    rPower.setTransBg()
                    rCardio.setColoredBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    btnSave.isEnabled = true
                }
                2 -> {
                    rPower.isChecked = true
                    etGroup.setText(wo!!.muscleGroup.toString())
                    etDesc.setText(wo!!.desc.toString())
                    etMins.setText("" + wo!!.workoutTime)
                    rRest.setTransBg()
                    rStretch.setTransBg()
                    rPower.setColoredBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    btnSave.isEnabled = true
                    Log.d("TAG", "workoutAdd: ${rPower.isChecked}")
                }
                3 -> {
                    rStretch.isChecked = true
                    etGroup.setText(wo!!.muscleGroup.toString())
                    etDesc.setText(wo!!.desc.toString())
                    etMins.setText("" + wo!!.workoutTime)
                    rRest.setTransBg()
                    rStretch.setColoredBg()
                    rPower.setTransBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.VISIBLE
                    popupContainer.viewForHide.visibility = View.VISIBLE
                    btnSave.isEnabled = true
                    Log.d("TAG", "workoutAdd: ${rStretch.isChecked}")
                }
                4 -> {
                    rRest.isChecked = true
                    etGroup.setText(wo!!.muscleGroup.toString())
                    etDesc.setText(wo!!.desc.toString())
                    etMins.setText("" + wo!!.workoutTime)
                    rRest.setColoredBg()
                    rStretch.setTransBg()
                    rPower.setTransBg()
                    rCardio.setTransBg()
                    layGrpAndTime.visibility = View.GONE
                    popupContainer.viewForHide.visibility = View.GONE
                    btnSave.isEnabled = true
                    Log.d("TAG", "workoutAdd: ${rRest.isChecked}")
                }


            }


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
                        btnSave.isEnabled = true
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
                        btnSave.isEnabled = true
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
                        btnSave.isEnabled = true
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
                        btnSave.isEnabled = true
                        Log.d("TAG", "workoutAdd: ${rCardio.isChecked}")
                    }
                }
            }

            rRest.setOnClickListener(rbListener)
            rStretch.setOnClickListener(rbListener)
            rPower.setOnClickListener(rbListener)
            rCardio.setOnClickListener(rbListener)

            btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
            btnSave.setOnClickListener { _ ->
                val category: Int = when {
                    rCardio.isChecked -> 1
                    rPower.isChecked -> 2
                    rStretch.isChecked -> 3
                    rRest.isChecked -> 4
                    else -> 0
                }
                val mins =
                    if (etMins.text.isNotEmpty()) Integer.parseInt(etMins.text.toString()) else 0

                runBlocking {
                    launch(Dispatchers.IO) {
                        appContainer.workoutsRepository.addWorkout(
                            workoutID,
                            category,
                            etGroup.text.toString(),
                            etDesc.text.toString(),
                            mins,
                            false
                        )
                    }
                }
                popupWindow.dismiss()
            }

            popupWindow.elevation = 20f
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
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

        // DEF SET
        layGrpAndTime.visibility = View.VISIBLE
        popupContainer.viewForHide.visibility = View.VISIBLE
        btnSave.isEnabled = false

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
                    btnSave.isEnabled = true
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
                    btnSave.isEnabled = true
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
                    btnSave.isEnabled = true
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
                    btnSave.isEnabled = true
                    Log.d("TAG", "workoutAdd: ${rCardio.isChecked}")
                }
            }
        }

        rRest.setOnClickListener(rbListener)
        rStretch.setOnClickListener(rbListener)
        rPower.setOnClickListener(rbListener)
        rCardio.setOnClickListener(rbListener)

        btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        btnSave.setOnClickListener { _ ->
            val category: Int = when {
                rCardio.isChecked -> 1
                rPower.isChecked -> 2
                rStretch.isChecked -> 3
                rRest.isChecked -> 4
                else -> 0
            }
            val mins = if (etMins.text.isNotEmpty()) Integer.parseInt(etMins.text.toString()) else 0

            runBlocking {
                launch(Dispatchers.IO) {
                    appContainer.workoutsRepository.addWorkout(
                        workoutID,
                        category,
                        etGroup.text.toString(),
                        etDesc.text.toString(),
                        mins,
                        false
                    )
                }
            }
            popupWindow.dismiss()
        }

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