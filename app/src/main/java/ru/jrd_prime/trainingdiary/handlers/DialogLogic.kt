package ru.jrd_prime.trainingdiary.handlers

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.pop_up_edit.view.*
import kotlinx.android.synthetic.main.pop_up_info.view.*
import org.threeten.bp.LocalDateTime
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.catIcons
import ru.jrd_prime.trainingdiary.utils.dateToTimestamp


fun setCategoryListeners(popupView: View) {
    val btnCardio = popupView.btnCardio // 1
    val btnPower = popupView.btnPower // 2
    val btnStretch = popupView.btnStretch // 3
    val btnRest = popupView.btnRest // 4
    popupView.btnSave.isEnabled = false
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

fun putDataToUI(wo: Workout, view: View) {
    setCategory(view, wo.category)
    view.etGroups.setText(wo.title)
    view.etDescription.setText(wo.description)
    view.etMinutes.setText(if (wo.time == 0) "" else wo.time.toString())
    view.etCalories.setText(if (wo.kcal == 0) "" else wo.kcal.toString())
    view.etDistance.setText(if (wo.distance == 0f) "" else wo.distance.toString())

}

fun putDataToInfoUI(wo: Workout, view: View) {
    val ctx = view.context
    val res = ctx.resources
    view.ivIcon.setImageResource(catIcons[wo.category] as Int)
    view.tvTitle.text = wo.getCheckedTitle(res)
    view.tvDesc.text = wo.getCheckedDescription(res)
    view.tvMinutes.text = wo.getFormattedMinutes(res)
    view.tvCalories.text = wo.getFormattedCalories(res)
    view.tvDistance.text = wo.getFormattedDistance(res)
    if (wo.category == 4) {
        setGone(view.puiDescContainer)
        setGone(view.puiMoreContainer)
    }
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

    val title = container.etGroups.text.toString()
    var desc = container.etDescription.text.toString()

    var minutes =
        if (container.etMinutes.text.isNotEmpty()) Integer.parseInt(container.etMinutes.text.toString()) else 0
    var calories =
        if (container.etCalories.text.isNotEmpty()) Integer.parseInt(container.etCalories.text.toString()) else 0
    var distance: Float =
        if (container.etDistance.text.isNotEmpty()) container.etDistance.text.toString()
            .toFloat() else 0f

    if (category == 4) {
        minutes = 0
        calories = 0
        distance = 0f
        desc = ""
    }

    return Workout(
        id = workoutID,
        category = category,
        title = title,
        description = desc,
        time = minutes,
        kcal = calories,
        distance = distance,
        date = dateToTimestamp(LocalDateTime.now()),
        empty = false
    )
}

fun setCategory(container: View, cat: Int) {
    when (cat) {
        0 -> {
            setAllBtnToFalse(container)
        }
        1 -> {
            container.btnCardio.isChecked = true
            container.btnSave.isEnabled = true
            container.btnCardio.setColoredBg()
            setVisbl(container.hideme)
        }
        2 -> {
            container.btnPower.isChecked = true
            container.btnSave.isEnabled = true
            container.btnPower.setColoredBg()
            setVisbl(container.hideme)
        }
        3 -> {
            container.btnStretch.isChecked = true
            container.btnSave.isEnabled = true
            container.btnStretch.setColoredBg()
            setVisbl(container.hideme)
        }
        4 -> {
            container.btnRest.isChecked = true
            container.btnSave.isEnabled = true
            container.btnRest.setColoredBg()
            setGone(container.hideme)
        }
    }
}

fun setRestChecked(container: View) {
    container.btnSave.isEnabled = true
    container.btnRest.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = true
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    setGone(container.hideme)
}

fun setStretchChecked(container: View) {
    container.btnSave.isEnabled = true
    container.btnStretch.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = true
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnRest.setTransBg()
    setVisbl(container.hideme)
}

fun setPowerChecked(container: View) {
    container.btnSave.isEnabled = true
    container.btnPower.setColoredBg()
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = true
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    setVisbl(container.hideme)
}

fun setCardioChecked(container: View) {
    container.btnSave.isEnabled = true
    container.btnCardio.setColoredBg()
    container.btnCardio.isChecked = true
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    setVisbl(container.hideme)
}

fun setAllBtnToFalse(container: View) {
    container.btnSave.isEnabled = false
    container.btnCardio.isChecked = false
    container.btnPower.isChecked = false
    container.btnStretch.isChecked = false
    container.btnRest.isChecked = false
    container.btnCardio.setTransBg()
    container.btnPower.setTransBg()
    container.btnStretch.setTransBg()
    container.btnRest.setTransBg()
    setVisbl(container.hideme)

}


private fun RadioButton.setTransBg() {
    setBackgroundResource(R.drawable.jp_radio_bg_transparent)
}

private fun RadioButton.setColoredBg() {
    setBackgroundResource(R.drawable.jp_radio_bg)
}


fun setGone(viewGroup: ViewGroup) {
    TransitionManager.beginDelayedTransition(viewGroup, AutoTransition())
    viewGroup.visibility = View.GONE
}

fun setVisbl(viewGroup: ViewGroup) {
    TransitionManager.beginDelayedTransition(viewGroup, AutoTransition())
    viewGroup.visibility = View.VISIBLE
}