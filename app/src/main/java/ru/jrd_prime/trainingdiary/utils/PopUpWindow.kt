package ru.jrd_prime.trainingdiary.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

fun getPopUpView(ctx: Context, layout: Int): View {
    return LayoutInflater.from(ctx).inflate(layout, null)
}

fun getPopUpWindow(view: View): PopupWindow {

    return PopupWindow(
        view,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        true
    )
}
