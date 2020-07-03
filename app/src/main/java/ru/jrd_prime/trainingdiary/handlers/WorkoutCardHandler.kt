package ru.jrd_prime.trainingdiary.handlers

import android.view.View
import android.widget.LinearLayout
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
        Toast.makeText(view.context, "Add $workoutID", Toast.LENGTH_SHORT).show()
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