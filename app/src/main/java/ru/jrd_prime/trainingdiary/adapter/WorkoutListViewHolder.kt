package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.MyHandler
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.Category.*
import ru.jrd_prime.trainingdiary.model.WorkoutModel


class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?, position: Int) {
        val cat = workoutCase?.workoutCategory
        Log.d("TAG", "bind: $position")
        val ivCategory = binding.ivCategory
        val cardLayout = binding.ivCategory
        val ivWeekDay = binding.ivWeekDay
        when (position) {
            0 -> ivWeekDay.setText("Mon")
            1 -> ivWeekDay.setText("Tue")
            2 -> ivWeekDay.setText("Wed")
            3 -> ivWeekDay.setText("Thu")
            4 -> ivWeekDay.setText("Fri")
            5 -> ivWeekDay.setText("Sat")
            6 -> ivWeekDay.setText("Sun")
        }
//
//        Log.d("dfpmsdfqlwpr1123", ((cat == Cardio).toString()))
//        Log.d("dfpmsdfqlwpr1123", ((cat == Rest).toString()))

        if (cat == Cardio) {
            ivCategory.setImageResource(R.drawable.jp_heartbeat)
            cardLayout.setBackgroundResource(R.drawable.card_bg_red)
//            binding.cardLayout.radius = 10.toFloat()
        }
        if (cat == Stretch) {
            ivCategory.setImageResource(R.drawable.jp_child)
            cardLayout.setBackgroundResource(R.drawable.card_bg_pink)
        }
        if (cat == Power) {
            ivCategory.setImageResource(R.drawable.jp_power)
            cardLayout.setBackgroundResource(R.drawable.card_bg_blue)
        }
        if (cat == Rest) {
            ivCategory.setImageResource(R.drawable.jp_sleep)
            cardLayout.setBackgroundResource(R.drawable.card_bg_yellow)
        }


        val myHandler = MyHandler()
        binding.handler = myHandler

//
//        val c: Calendar = Calendar.getInstance()
//        c.setTime(workoutCase.workoutDate)
//        val dayOfWeek: Int = c.get(Calendar.DAY_OF_WEEK)


//        Log.d("TAG", "bind: ${cat.toString()}")

        binding.workoutModel = workoutCase
        binding.executePendingBindings()
    }

}