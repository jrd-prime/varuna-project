package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.MyHandler
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.Category.*
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.util.*


class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?) {
        val cardio = Cardio
        val power = Power
        val rest = Rest
        val cat = workoutCase?.workoutCategory
        when (cat) {
            Cardio -> binding.ivCategory.setImageResource(R.drawable.jp_heartbeat)
            Stretch -> binding.ivCategory.setImageResource(R.drawable.jp_sleep)
            Power -> binding.ivCategory.setImageResource(R.drawable.jp_dumbbell)
            Rest -> binding.ivCategory.setImageResource(R.drawable.jp_sleep)
            null -> binding.ivCategory.setImageResource(R.drawable.ic_logout)
        }
        val myHandler = MyHandler()
        binding.handler = myHandler

//
//        val c: Calendar = Calendar.getInstance()
//        c.setTime(workoutCase.workoutDate)
//        val dayOfWeek: Int = c.get(Calendar.DAY_OF_WEEK)



        Log.d("TAG", "bind: ${cat.toString()}")

        binding.workoutModel = workoutCase
        binding.executePendingBindings()
    }

}