package ru.jrd_prime.trainingdiary.ui

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.catColor
import ru.jrd_prime.trainingdiary.utils.catIcons
import ru.jrd_prime.trainingdiary.utils.getMonthDayFromDate
import java.text.SimpleDateFormat
import java.util.*

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(workoutCase: WorkoutModel?, position: Int) {
        if (workoutCase == null) {
            //TODO Что мы делаем если нету кейса
            Log.d("JP_TAG", "bind: WORKOUTCASE NULL!")
        } else {
//            Log.d("TAG", "bind: wdate ${SimpleDateFormat("dd.MM.yyyy").format(wCase.workoutDate)}")
            val wCategory: Int = workoutCase.workoutCategory
            val daysOfWeek = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            /* WORKOUT CARD */
            val mainCardLay = binding.cardLay
            val frameForHide = binding.frameForHide
            val mainWeekDay = binding.ivWeekDay
            val mainMonthDay = binding.ivMonthDay
            val dot = binding.dot
            val ivCategory: ImageView = binding.ivCategory
            val title = binding.tvMuscleGroup
            val desc = binding.cardDescription
            val time = binding.tvWorkoutTime

            mainMonthDay.text = SimpleDateFormat("dd").format(workoutCase.workoutDate)
            /* EMPTY CARD */
            val emptyCardLay = binding.cardOverLay
            val emptyWeekDay = binding.ivWeekDay1
            val emptyMonthDay = binding.ivMonthDay1
            val emptyImageView = binding.ivEmptyCategory

            // Устанавливаем дату
            mainWeekDay.text = daysOfWeek[position]
            emptyWeekDay.text = daysOfWeek[position]
            // Устанавливаем день недели
            mainMonthDay.text = getMonthDayFromDate(Date(workoutCase.workoutDate!!))
            emptyMonthDay.text = getMonthDayFromDate(Date(workoutCase.workoutDate))

            if (!workoutCase.workoutEmpty) {
//                Log.d("JP_TAG", "bind: WORKOUTCASE NOT EMPTY")
                frameForHide.visibility = View.GONE
                emptyCardLay.visibility = View.GONE
                mainCardLay.visibility = View.VISIBLE
                title.text = workoutCase.muscleGroup
                desc.text = workoutCase.desc


                var timeString = ""
                val timeInt = workoutCase.workoutTime

                if (timeInt == 0) {
                    binding.l3topcut.visibility = View.GONE
                    binding.tvWorkoutTime.visibility = View.GONE
                }

                if (workoutCase.desc.isEmpty()) {
                    binding.cardLineSecond.visibility = View.GONE
                    binding.lineOverDesc.visibility = View.GONE
                }

                when {
                    timeInt < 60 -> timeString = "${timeInt.toString()} mins"
                    timeInt >= 60 -> {
                        val mins = timeInt % 60
                        val hrs = (timeInt - (timeInt % 60)) / 60

                        timeString = when (mins) {
                            0 -> ""
                            1 -> "${mins.toString()} min"
                            else -> "${mins.toString()} mins"
                        }

                        timeString = if (hrs == 1) {
                            "${hrs.toString()} hour $timeString"
                        } else {
                            "${hrs.toString()} hours $timeString"
                        }

                    }
                }



                if (workoutCase.workoutCategory == 4) {

                    binding.cardLineSecond.visibility = View.GONE
                    binding.lineOverDesc.visibility = View.GONE
                    if (workoutCase.muscleGroup.isEmpty()) {
                        if (workoutCase.desc.isEmpty()) {

                            binding.tvMuscleGroup.text = "No description"
                        } else {
                            binding.tvMuscleGroup.text = workoutCase.desc
                        }
                    }
                } else {
                    if (workoutCase.muscleGroup.isEmpty()) {

                        binding.tvMuscleGroup.text = "No description"
                        binding.tvMuscleGroup.setTextColor(Color.parseColor("#eeeeee"))
                    }
                }
                time.text = timeString
            } else {
//                Log.d("JP_TAG", "WORKOUTCASE EMPTY")
                setImageViewAndDot(0, emptyImageView, dot)
                mainCardLay.visibility = View.GONE
                emptyCardLay.visibility = View.VISIBLE
            }

            if (wCategory != 0) {
                /* Категория есть */
                setImageViewAndDot(workoutCase.workoutCategory, ivCategory, dot)
            } else {
                /* Категории нету */
                setImageViewAndDot(0, ivCategory, dot)
            }

            binding.workoutModel = workoutCase // Отправляем кейс в карточку
        }
        binding.executePendingBindings()
    }

    private fun setImageViewAndDot(
        categoryID: Int,
        ivCategory: ImageView,
        dot: FrameLayout
    ) {
        ivCategory.setImageResource(catIcons[categoryID] as Int)
        dot.setBackgroundResource(catColor[categoryID] as Int)
    }

}