package ru.jrd_prime.trainingdiary.handlers

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import ru.jrd_prime.trainingdiary.R

class WorkoutCardHandler {
    fun workoutDelete(view: View, workoutID: Int) {
        Toast.makeText(view.context, "Del $workoutID", Toast.LENGTH_SHORT).show()
        Snackbar.make(view, "asdasd", Snackbar.LENGTH_LONG)
            .setAction("Cancel", View.OnClickListener { v ->
                Toast.makeText(view.context, "Cancelled $workoutID", Toast.LENGTH_SHORT).show()
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