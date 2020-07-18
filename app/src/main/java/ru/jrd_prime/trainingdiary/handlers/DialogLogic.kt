package ru.jrd_prime.trainingdiary.handlers

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.pop.view.*
import org.threeten.bp.LocalDateTime
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.dateToTimestamp


fun setCategoryListeners(popupView: View) {
    val btnCardio = popupView.btnCardio // 1
    val btnPower = popupView.btnPower // 2
    val btnStretch = popupView.btnStretch // 3
    val btnRest = popupView.btnRest // 4
    val rbListener = View.OnClickListener {
        when {
            btnCardio.isChecked -> setCardioChecked(popupView)
            btnPower.isChecked -> setPowerChecked(popupView)
            btnStretch.isChecked -> setStretchChecked(popupView)
            btnRest.isChecked -> setRestChecked(popupView)
        }
    }

    btnCardio.setOnClickListener(rbListener)
    btnPower.setOnClickListener(rbListener)
    btnStretch.setOnClickListener(rbListener)
    btnRest.setOnClickListener(rbListener)

}

fun putDataToUI(wo: Workout, container: View) {
    Log.d("TAGGGGGG", "putDataToUI: $wo")
    val cat = wo.category
    val grp = wo.title
    val time = wo.time
    val desc = wo.description
    setCategory(container, cat)
    container.etGroups.setText(grp.toString())
    container.etDescription.setText(desc.toString())
    container.etMins.setText(time.toString())
}


fun collectDataFromUI(
    container: View,
    workoutID: String
): Workout {
    val category: Int = when {
        container.btnCardio.isChecked -> 1
        container.btnPower.isChecked -> 2
        container.btnStretch.isChecked -> 3
        container.btnRest.isChecked -> 4
        else -> 0
    }

    var grp = container.etGroups.text.toString()

    var mins =
        if (container.etMins.text.isNotEmpty()) Integer.parseInt(container.etMins.text.toString()) else 0

    if (category == 4) {
        mins = 0
        grp = ""
    }

    return Workout(
        workoutID,
        category,
        grp,
        container.etDescription.text.toString(),
        mins,
        dateToTimestamp(LocalDateTime.now()),
        empty = false
    )
}

fun setCategory(container: View, cat: Int) {
    when (cat) {
        0 -> setAllBtnToFalse(container)
        1 -> {
            container.btnCardio.isChecked = true
            container.btnCardio.setColoredBg()
            container.layoutGroupsAndTime.visibility = View.VISIBLE
        }
        2 -> {
            container.btnPower.isChecked = true
            container.btnPower.setColoredBg()
            container.layoutGroupsAndTime.visibility = View.VISIBLE
        }
        3 -> {
            container.btnStretch.isChecked = true
            container.btnStretch.setColoredBg()
            container.layoutGroupsAndTime.visibility = View.VISIBLE
        }
        4 -> {
            container.btnRest.isChecked = true
            container.btnRest.setColoredBg()
            container.layoutGroupsAndTime.visibility = View.GONE
        }
    }
}

fun setRestChecked(container: View) {
    container.btnRest.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = true
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    container.layoutGroupsAndTime.visibility = View.GONE
}

fun setStretchChecked(container: View) {
    container.btnStretch.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = true
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnRest.setTransBg()
    container.layoutGroupsAndTime.visibility = View.VISIBLE
}

fun setPowerChecked(container: View) {
    container.btnPower.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = true
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    container.layoutGroupsAndTime.visibility = View.VISIBLE
}

fun setCardioChecked(container: View) {
    container.btnCardio.setColoredBg()
    container.btnCardio.isChecked = true
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    container.layoutGroupsAndTime.visibility = View.VISIBLE
}

fun setAllBtnToFalse(container: View) {
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    container.layoutGroupsAndTime.visibility = View.VISIBLE

}


private fun RadioButton.setTransBg() {
    setBackgroundResource(R.drawable.jp_radio_bg_transparent)
}

private fun RadioButton.setColoredBg() {
    setBackgroundResource(R.drawable.jp_radio_bg)
}


 fun setGone(contView: LinearLayout) {
    TransitionManager.beginDelayedTransition(contView, AutoTransition())
    contView.visibility = View.GONE
}

fun setVisible(contView: LinearLayout) {
    TransitionManager.beginDelayedTransition(contView, AutoTransition())
    contView.visibility = View.VISIBLE
}