package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.handlers.WorkoutCardHandler
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.getMonthDayFromDate
import java.util.*

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?, position: Int) {


        val daysOfWeek = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        if (workoutCase == null) {
            //TODO Что мы делаем если нету кейса
            Log.d("JP_TAG", "bind: WORKOUTCASE NULL!")
        } else {

            if (!workoutCase.workoutEmpty) {

                binding.cardOverLay.visibility = View.GONE
                val cat = workoutCase.workoutCategory
                val myHandler = WorkoutCardHandler(binding.root)
                val ivCategory = binding.ivCategory
                val cardLayout = binding.dot
                val ivWeekDay = binding.ivWeekDay
                val workoutCard = binding.wCard
                val ivMonthDay = binding.ivMonthDay

                binding.handler = myHandler


                /* CARD*/
                workoutCard.elevation = 4f
                ivWeekDay.text = daysOfWeek[position]


                ivMonthDay.text = getMonthDayFromDate(Date(workoutCase.workoutDate))


//            if (cat == Cardio) {
//                ivCategory.setImageResource(R.drawable.jp_heartbeat)
//                cardLayout.setBackgroundResource(R.drawable.card_bg_red)
//            }
//            if (cat == Stretch) {
//                ivCategory.setImageResource(R.drawable.jp_child)
//                cardLayout.setBackgroundResource(R.drawable.card_bg_pink)
//            }
//            if (cat == Power) {
//                ivCategory.setImageResource(R.drawable.jp_dumbbell)
//                cardLayout.setBackgroundResource(R.drawable.card_bg_blue)
//            }
//            if (cat == Rest) {
//                ivCategory.setImageResource(R.drawable.jp_sleep)
//                cardLayout.setBackgroundResource(R.drawable.card_bg_yellow)
//            }
            } else if (workoutCase.workoutEmpty) {

                val cat = workoutCase.workoutCategory
                val myHandler = WorkoutCardHandler(binding.root)
                val ivCategory = binding.ivCategory
                val cardLayout = binding.dot
                val ivWeekDay = binding.ivWeekDay
                val workoutCard = binding.wCard
                val ivMonthDay = binding.ivMonthDay

                binding.cardLay.visibility = View.GONE

                binding.ivWeekDay1.text = daysOfWeek[position]


                binding.ivMonthDay1.text = getMonthDayFromDate(Date(workoutCase.workoutDate))
                binding.ivEmptyCategory.setBackgroundResource(R.drawable.jp_question)

                binding.cardOverLay.visibility = View.VISIBLE
            }
            binding.workoutModel = workoutCase
        }
        binding.executePendingBindings()
    }

}