package ru.jrd_prime.trainingdiary.handlers

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.a_workout_card.view.*
import kotlinx.android.synthetic.main.pop.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.workers.AsyncRequests


class WorkoutCardHandler(root: View) {
    private val appContainer = (root.context.applicationContext as TrainingDiaryApp).container
    private val asyncReq: AsyncRequests = AsyncRequests(appContainer)
    private val ctx: Context = root.context

    fun workoutDelete(view: View, workoutID: Int) {
        asyncReq.clearWorkout(workoutID) /* Set workout empty = true */
        val snack = Snackbar.make(view, R.string.snack_record_deleted, 7000)
        val snackView = snack.view
        snack.setAction(R.string.snack_restore) {
            asyncReq.restoreWorkout(workoutID) /* Set workout empty = false */
        }
        snack.setActionTextColor(ctx.getColor(R.color.colorSnackbarButton))
        snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ctx.getColor(R.color.colorLightGrey))
        snackView.setBackgroundResource(R.drawable.snack_bg)
        snack.show()
    }


    fun workoutAdd(view: View, workoutID: Int) {
        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop, null)
        popupView.textTitle.setText(R.string.add_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        setCategoryListeners(popupView)
        popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        popupView.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(popupView, workoutID)
            asyncReq.updateWorkout(workoutID, dataFromUI)

            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    fun workoutEdit(view: View, workoutID: Int) {
        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop, null)
        popupView.textTitle.setText(R.string.edit_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        val wo: WorkoutModel? = asyncReq.getWorkout(workoutID)


        if (wo == null) {
            Toast.makeText(view.context, "Error on get workout ID: $workoutID", Toast.LENGTH_SHORT)
                .show()
        } else {
            // main container
            putDataToUI(wo, popupView)
            setCategoryListeners(popupView)
            popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
            popupView.btnSave.setOnClickListener { _ ->
                val dataFromUI = collectDataFromUI(popupView, workoutID)
                asyncReq.updateWorkout(workoutID, dataFromUI)
                popupWindow.dismiss()
            }
            popupWindow.elevation = 20f
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        }
    }


    fun showAdditionalInfo(view: View?) {
        if (view == null) return
        val contView = view.frameForHide

        when (contView.visibility) {
            View.GONE -> setVisible(contView)
            View.VISIBLE -> setGone(contView)
        }
    }
}