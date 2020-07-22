package ru.jrd_prime.trainingdiary.handlers

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.pop_up_edit.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.workers.AsyncRequests

class CardHandler(root: View) {
    private val appContainer = (root.context.applicationContext as TrainingDiaryApp).container
    private val asyncReq: AsyncRequests = AsyncRequests(appContainer)
    private val ctx: Context = root.context

    companion object {
        const val TAG = "CardHandler"
        var rotationAngle = 0f
    }

    fun addMainWorkout(view: View, workoutDate: String) {
        Log.d(TAG, "workoutAdd: $workoutDate")

        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop_up_edit, null)
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
            val dataFromUI = collectDataFromUI(popupView, workoutDate)
            asyncReq.updateWorkout(workoutDate, dataFromUI)

            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }


}