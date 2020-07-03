package ru.jrd_prime.trainingdiary.handlers

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageSwitcher
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
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
        val context = view.context
//        Toast.makeText(view.context, "Add $workoutID", Toast.LENGTH_SHORT).show()

        val popupView: View =
            LayoutInflater.from(context).inflate(R.layout.pop, null)


        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )



        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
// define your view here that found in popup_layout
// for example let consider you have a button


        // If you need the PopupWindow to dismiss when when touched outside

        // If you need the PopupWindow to dismiss when when touched outside
//        popupWindow.setBackgroundDrawable(ColorDrawable())


// finally show up your popwindow

//        popupWindow.showAsDropDown(popupView, 0, 0)

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