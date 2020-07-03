package ru.jrd_prime.trainingdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.handlers.WorkoutCardHandler
import ru.jrd_prime.trainingdiary.model.WorkoutModel


class WorkoutListAdapter :
    RecyclerView.Adapter<WorkoutListViewHolder>() {
    private var items: MutableList<WorkoutModel> = mutableListOf<WorkoutModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: AWorkoutCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.a_workout_card, parent, false)
        binding.handler = WorkoutCardHandler(binding.root)
        return WorkoutListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        holder.bind(items.get(position), position)
    }

    fun setNewData(newData: List<WorkoutModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
