package ru.jrd_prime.trainingdiary.utils

import android.content.Context
import android.widget.Toast

class Toasts(private val context: Context) {
    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun errorOnGoogleSignIn(err: Int) {
        val msg = "signInResult: failed code= $err"
        showToast(msg)
    }

}