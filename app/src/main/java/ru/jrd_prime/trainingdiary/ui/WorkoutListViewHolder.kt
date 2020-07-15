package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.catColor
import ru.jrd_prime.trainingdiary.utils.catIcons
import ru.jrd_prime.trainingdiary.utils.lg
import ru.jrd_prime.trainingdiary.utils.minutesToHoursAndMinutes

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(_binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(_binding.root) {
    private val binding: AWorkoutCardBinding = _binding
    private val root: View = binding.root
    private val ctx: Context = root.context
    private val res: Resources = ctx.resources

    // clr
    private val clrLightForText = ContextCompat.getColor(ctx, R.color.colorForLightText)
    private val clrDarkGrey = ContextCompat.getColor(ctx, R.color.colorGreyDark)

    // str
    private val strNoTitle = res.getString(R.string.no_title)
    private val strNoDesc = res.getString(R.string.no_description)

    fun bind(workout: Workout?, position: Int) {
        if (workout == null) {
            //TODO Что мы делаем если нету кейса
            Log.d("JP_TAG", "bind: WORKOUTCASE NULL!")
        } else {

            Log.d(TAG, "bind: workout : $workout")

            /* DEF HIDE */
            hide(binding.frameForHide)
            hide(binding.cardOverLay)

            steCategoryImage(workout.category)

            val weekDays = res.getStringArray(R.array.weekDays)
            // Устанавливаем дату
            binding.ivWeekDay.text = weekDays[position]
            binding.ivWeekDay1.text = weekDays[position]
            // Устанавливаем день недели
            val date = workout.id.split("-")[2]
            binding.ivMonthDay.text = date
            binding.ivMonthDay1.text = date

            when (workout.empty) {
                true -> showEmptyView()
                false -> showClassicView(workout)
            }
            binding.workoutModel = workout // Отправляем кейс в карточку
        }
        binding.executePendingBindings()
    }

    private fun showClassicView(
        workout: Workout
    ) {
        val cat = workout.category
        val title = workout.title
        val desc = workout.description
        val time = workout.time

        binding.tvMuscleGroup.setTextColor(clrDarkGrey)
        binding.textDescription.setTextColor(clrDarkGrey)
        binding.textTime.setTextColor(clrDarkGrey)
        show(binding.textDescription)
        show(binding.timeContainer)
        show(binding.textTime)

        if (cat == 4) { // if REST
            if (desc.isEmpty()) { // desc empty
                workout.title = strNoDesc
                binding.tvMuscleGroup.setTextColor(clrLightForText)
                hide(binding.textDescription)
                hide(binding.timeContainer)
            } else { // desc OK
                workout.title = desc
                hide(binding.timeContainer)
                hide(binding.textDescription)
            }
        } else if (cat == 0) { // if EMPTY
            lg("showClassicView", "EMPTY")
        } else { // ELSE
            if (title.isEmpty()) {
                workout.title = strNoTitle
                binding.tvMuscleGroup.setTextColor(clrLightForText)
            }
            if (desc.isEmpty()) {
                workout.description = strNoDesc
                binding.textDescription.setTextColor(clrLightForText)
            }
            if (time == 0) {
                hide(binding.textTime)
            } else {
                binding.textTime.text = minutesToHoursAndMinutes(time, res)
                show(binding.timeContainer)
            }
        }
    }

    private fun showEmptyView() {
        show(binding.cardOverLay)
    }

    private fun hide(view: View) {
        view.visibility = View.GONE
    }

    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun steCategoryImage(catId: Int) {
        if (catId != 0) {
            /* Категория есть */
            setImageFromConstants(catId, binding.ivCategory, binding.dot)
        } else {
            /* Категории нету */
            setImageFromConstants(0, binding.ivCategory, binding.dot)
        }
    }

    private fun setImageFromConstants(catId: Int, catIv: ImageView, dot: FrameLayout) {
        catIv.setImageResource(catIcons[catId] as Int)
        dot.setBackgroundResource(catColor[catId] as Int)
    }
}