package ru.jrd_prime.trainingdiary.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.pop_up_yes_no.view.*
import ru.jrd_prime.trainingdiary.R

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

fun makeDialogYesOrNo(
    ctx: Context,
    root: View,
    yesListener: View.OnClickListener,
    title: Int
) {
    val view: View = getPopUpView(ctx, R.layout.pop_up_yes_no)
    val window = getPopUpWindow(view)

    view.popUpYesNoTitle.text = ctx.resources.getString(title)
    view.popUpYesNoDescCont.visibility = View.GONE
    view.popUpYesNoBtnYes.setOnClickListener(yesListener)
    view.popUpYesNoBtnNo.setOnClickListener { _ -> window.dismiss() }

    root.post(Runnable { window.showAtLocation(root, Gravity.BOTTOM, 0, 0) })
}
