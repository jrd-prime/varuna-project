package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.MyHandler
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.Category.*
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.text.SimpleDateFormat
import java.util.*


class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?, position: Int) {
        val cat = workoutCase?.workoutCategory
        Log.d("TAG", "bind: $position")
        val ivCategory = binding.ivCategory
//        val cardLayout = binding.leftCut
        val cardLayout = binding.dot
        val ivWeekDay = binding.ivWeekDay

        val workoutCard = binding.wCard


        /* CARD*/

        workoutCard.elevation = 4f
//        workoutCard.radius = 4f

        val daysOfWeek = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        ivWeekDay.text = daysOfWeek[position]

//        when (position) {
//            0 -> ivWeekDay.text = daysOfWeek[0]
//            1 -> ivWeekDay.text = "Tue"
//            2 -> ivWeekDay.text = "Wed"
//            3 -> ivWeekDay.text = "Thu"
//            4 -> ivWeekDay.text = "Fri"
//            5 -> ivWeekDay.text = "Sat"
//            6 -> ivWeekDay.text = "Sun"
//        }


        val da: Date = workoutCase?.workoutDate as Date

        val form = SimpleDateFormat("dd")
        val str = form.format(da)

        binding.ivMonthDay.text = str


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
            ivCategory.setImageResource(R.drawable.jp_dumbbell)
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