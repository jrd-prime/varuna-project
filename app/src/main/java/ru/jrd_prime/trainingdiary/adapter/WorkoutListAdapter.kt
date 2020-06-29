package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AWorkoutCardBinding
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.util.*


class WorkoutListAdapter(val data: Collection<WorkoutModel>) : RecyclerView.Adapter<WorkoutListViewHolder>() {
    private val items: MutableList<WorkoutModel> = data as MutableList<WorkoutModel>


//    fun setData() {
//        items.clear()
//        items.addAll(data)
//        Log.d("TAG", "setData: $items" )
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: AWorkoutCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.a_workout_card, parent, false)
        return WorkoutListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        Log.d("TAGGGG", "getItemCount: ${items.size}")
        return items.size
    }
}