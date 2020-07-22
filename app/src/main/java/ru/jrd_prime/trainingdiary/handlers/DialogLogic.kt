package ru.jrd_prime.trainingdiary.handlers

import android.util.Log
import android.view.View
import android.widget.LinearLayout
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

    setCategory(container, wo.category)
    container.etGroups.setText(wo.title.toString())
    container.etDescription.setText(wo.description.toString())
    container.etMinutes.setText(wo.time.toString())
    container.etCalories.setText(wo.kcal.toString())
    container.etDistance.setText(wo.distance.toString())
}

fun putDataToInfoUI(wo: Workout, container: View) {
    val ctx = container.context
    Log.d("TAGGGGGG", "putDataToInfoUI: $wo")
    container.ivIcon.setImageResource(catIcons[wo.category] as Int)
    container.tvTitle.text = wo.title.toString()
    container.tvDesc.text = wo.description.toString()
    container.tvMinutes.text =
        String.format(ctx.getString(R.string.minutes_val), wo.time.toString())

    container.tvCalories.text =
        String.format(ctx.getString(R.string.calories_val), wo.kcal.toString())
    container.tvDistance.text =
        String.format(ctx.getString(R.string.distance_val), wo.distance.toString())
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

    var title = container.etGroups.text.toString()

    var minutes =
        if (container.etMinutes.text.isNotEmpty()) Integer.parseInt(container.etMinutes.text.toString()) else 0
    var calories =
        if (container.etMinutes.text.isNotEmpty()) Integer.parseInt(container.etCalories.text.toString()) else 0
    var distance: Float =
        if (container.etMinutes.text.isNotEmpty()) container.etDistance.text.toString()
            .toFloat() else 0f

    if (category == 4) {
        minutes = 0
        calories = 0
        distance = 0f
        title = ""
    }

    return Workout(
        id = workoutID,
        category = category,
        title = title,
        description = container.etDescription.text.toString(),
        time = minutes,
        kcal = calories,
        distance = distance,
        date = dateToTimestamp(LocalDateTime.now()),
        empty = false
    )
}

fun setCategory(container: View, cat: Int) {
    when (cat) {
        0 -> setAllBtnToFalse(container)
        1 -> {
            container.btnCardio.isChecked = true
            container.btnCardio.setColoredBg()
            container.hideme.visibility = View.VISIBLE
        }
        2 -> {
            container.btnPower.isChecked = true
            container.btnPower.setColoredBg()
            container.hideme.visibility = View.VISIBLE
        }
        3 -> {
            container.btnStretch.isChecked = true
            container.btnStretch.setColoredBg()
            container.hideme.visibility = View.VISIBLE
        }
        4 -> {
            container.btnRest.isChecked = true
            container.btnRest.setColoredBg()
            container.hideme.visibility = View.GONE
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
    container.hideme.visibility = View.GONE
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
    container.hideme.visibility = View.VISIBLE
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
    container.hideme.visibility = View.VISIBLE
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
    container.hideme.visibility = View.VISIBLE
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
    container.hideme.visibility = View.VISIBLE

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