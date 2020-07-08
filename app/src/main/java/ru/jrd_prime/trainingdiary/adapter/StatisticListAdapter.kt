package ru.jrd_prime.trainingdiary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.AStatPlaceItemBinding
import ru.jrd_prime.trainingdiary.model.PlaceStatisticModel
import ru.jrd_prime.trainingdiary.ui.StatisticListViewHolder


class StatisticListAdapter : RecyclerView.Adapter<StatisticListViewHolder>() {

    private var data: MutableList<PlaceStatisticModel> = mutableListOf<PlaceStatisticModel>()


    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticListViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding: AStatPlaceItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.a_stat_place_item, parent, false)


//        binding.handler = WorkoutCardHandler(binding.root)
        return StatisticListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticListViewHolder, position: Int) {
        holder.bind(data[position], position, data[0].catPercent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setNewData(list: List<PlaceStatisticModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}