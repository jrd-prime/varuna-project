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

//
//        Log.d("dfpmsdfqlwpr1123", ((cat == Cardio).toString()))
//        Log.d("dfpmsdfqlwpr1123", ((cat == Rest).toString()))

        if (cat == Cardio) {
            binding.ivCategory.setImageResource(R.drawable.jp_heartbeat)
            Log.d("TAG", "bind: SET CARDIO")
        }
        if (cat == Stretch) binding.ivCategory.setImageResource(R.drawable.ic_logout)
        if (cat == Power) binding.ivCategory.setImageResource(R.drawable.jp_dumbbell)
        if (cat == Rest) binding.ivCategory.setImageResource(R.drawable.jp_sleep)

        binding.ivWeekDay.text = (position + 1).toString()

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