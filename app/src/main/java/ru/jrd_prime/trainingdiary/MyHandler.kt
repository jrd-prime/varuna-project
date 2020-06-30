package ru.jrd_prime.trainingdiary

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

class MyHandler {


    fun setImg(view: View?, category: String?) {

//        Log.d("afaddasd", "setImg: $category")


    }
    fun onGo(view: View?) {
        if (view == null) return
        val contView = view.findViewById<ConstraintLayout>(R.id.frameForHide)

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

    private fun setGone(contView: ConstraintLayout) {
        Toast.makeText(contView.context, "HIDE", Toast.LENGTH_SHORT).show()
        TransitionManager.beginDelayedTransition(contView, AutoTransition())
        contView.visibility = View.GONE
    }

    private fun setVisible(contView: ConstraintLayout) {
        Toast.makeText(contView.context, "SHOW", Toast.LENGTH_SHORT).show()
        TransitionManager.beginDelayedTransition(contView, AutoTransition())
        contView.visibility = View.VISIBLE
    }
}