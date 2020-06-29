package ru.jrd_prime.trainingdiary.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.WorkoutModel


class WorkoutListViewHolder(private var binding: AWorkoutCardBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutCase: WorkoutModel?) {
        binding.workoutModel = workoutCase
        binding.executePendingBindings()
    }

}