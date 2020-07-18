package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AAdditionalItemCardBinding
import ru.jrd_prime.trainingdiary.databinding.ANewCardViewBinding
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.catColor
import ru.jrd_prime.trainingdiary.utils.catIcons
import ru.jrd_prime.trainingdiary.utils.lg
import ru.jrd_prime.trainingdiary.utils.minutesToHoursAndMinutes

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(_binding: ANewCardViewBinding) :
    RecyclerView.ViewHolder(_binding.root) {
    private val binding: ANewCardViewBinding = _binding
    private val root: View = binding.root
    private val ctx: Context = root.context
    private val res: Resources = ctx.resources
    private val li = LayoutInflater.from(ctx)

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
//            Log.d(TAG, "bind: moreWork: $moreWorkouts")
            val adds = workout.additional

            /* DEF HIDE */
//            binding.hideThis.visibility = View.GONE //TODO uncomment on release


            var notEmpty = 0

            if (!adds.isNullOrEmpty()) {
                /* ADDITIONAL NOT EMPTY */
                Log.d(TAG, "workouts: ${adds}")
                adds.removeAt(0) // remove "null" record
                val fullSize = adds.size

                Log.d(TAG, "fullsize: ${fullSize}")

                for (ad in adds) if (!ad.empty) notEmpty += 1


                Log.d(TAG, "workouts: ${adds}")
                if (notEmpty != 0) {
                    for (i in 0 until notEmpty) inflateCard(adds[i])
                }


                Log.d(TAG, "not empty size: $notEmpty")

            } else {
//                inflateAddCard()
            }


            if (notEmpty <= 2) inflateAddCard()



            steCategoryImage(workout.category)

            val weekDays = res.getStringArray(R.array.weekDays)
            binding.ivWeekDay.text = weekDays[position]// Устанавливаем дату

            val date = workout.id.split("-")[2]
            binding.ivMonthDay.text = date// Устанавливаем день недели

            when (workout.empty) {
                true -> showEmptyView()
                false -> showClassicView(workout)
            }
            binding.workoutModel = workout // Отправляем кейс в карточку
        }
        binding.executePendingBindings()
    }

    private fun inflateAddCard() {
        val vi = li.inflate(R.layout.a_additional_add_card, null)
        binding.additionalCardsHolder.removeView(vi)
        binding.additionalCardsHolder.addView(vi)
    }

    private fun inflateCard(workout: Workout) {
        val additionalCardItemBinding: AAdditionalItemCardBinding = DataBindingUtil.inflate(
            li,
            R.layout.a_additional_item_card,
            binding.additionalCardsHolder,
            true
        )
        additionalCardItemBinding.wo = workout
    }

    private fun showEmptyAdditionalPanelForId(panelId: Int) {

        Log.d(TAG, "showEmptyAdditionalPanelForId(panelId = $panelId)")
    }

    private fun showAdditionalPanels(panelsCount: Int) {
        Log.d(TAG, "showAdditionalPanels(panelsCount = $panelsCount)")

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
//        show(binding.cardOverLay)
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