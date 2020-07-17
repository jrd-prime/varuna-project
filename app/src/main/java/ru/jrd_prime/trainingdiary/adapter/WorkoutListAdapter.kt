package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.ANewCardViewBinding
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.handlers.WorkoutCardHandler
import ru.jrd_prime.trainingdiary.ui.WorkoutListViewHolder


class WorkoutListAdapter :
    RecyclerView.Adapter<WorkoutListViewHolder>() {
    companion object {
        const val TAG = "Workout List Adapter"
    }

    private var items: MutableList<Workout> = mutableListOf<Workout>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ANewCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.a_new_card_view, parent, false)
        binding.handler = WorkoutCardHandler(binding.root)
        return WorkoutListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun setNewData(newData: MutableList<Workout>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(weekData: Workout) {
        items.add(weekData)
        notifyDataSetChanged()
    }

    fun updateItem(key: String, workout: Workout) {
        val index = items.indexOfFirst { it.id == key }
        if (index >= 0) {
            items[index] = workout
            Log.d(TAG, "updateItem: FINDED. key = $key, $index, REPLACED")
            notifyDataSetChanged()
        }
    }
}
