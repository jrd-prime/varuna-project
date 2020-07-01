package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.handlers.WorkoutCardHandler
import ru.jrd_prime.trainingdiary.model.Category.*
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.getMonthDayFromDate
import java.util.*

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?, position: Int) {
        if (workoutCase == null) {
            //TODO Что мы делаем если нету кейса
            Log.d("JP_TAG", "bind: WORKOUTCASE NULL!")
        } else {

            val cat = workoutCase.workoutCategory
            val myHandler = WorkoutCardHandler()
            val ivCategory = binding.ivCategory
            val cardLayout = binding.dot
            val ivWeekDay = binding.ivWeekDay
            val workoutCard = binding.wCard
            val ivMonthDay = binding.ivMonthDay
            val daysOfWeek = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

            binding.handler = myHandler


            /* CARD*/
            workoutCard.elevation = 4f
            ivWeekDay.text = daysOfWeek[position]


            ivMonthDay.text = getMonthDayFromDate(workoutCase.workoutDate as Date)


            if (cat == Cardio) {
                ivCategory.setImageResource(R.drawable.jp_heartbeat)
                cardLayout.setBackgroundResource(R.drawable.card_bg_red)
            }
            if (cat == Stretch) {
                ivCategory.setImageResource(R.drawable.jp_child)
                cardLayout.setBackgroundResource(R.drawable.card_bg_pink)
            }
            if (cat == Power) {
                ivCategory.setImageResource(R.drawable.jp_dumbbell)
                cardLayout.setBackgroundResource(R.drawable.card_bg_blue)
            }
            if (cat == Rest) {
                ivCategory.setImageResource(R.drawable.jp_sleep)
                cardLayout.setBackgroundResource(R.drawable.card_bg_yellow)
            }

            binding.workoutModel = workoutCase
        }
        binding.executePendingBindings()
    }

}