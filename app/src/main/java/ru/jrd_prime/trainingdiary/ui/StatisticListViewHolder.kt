package ru.jrd_prime.trainingdiary.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import ru.jrd_prime.trainingdiary.databinding.AStatPlaceItemBinding
import ru.jrd_prime.trainingdiary.model.PlaceStatisticModel
import ru.jrd_prime.trainingdiary.utils.catIcons


class StatisticListViewHolder(private var binding: AStatPlaceItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        placeItem: PlaceStatisticModel?,
        position: Int,
        maxPercent: Float
    ) {
        if (placeItem != null) {
            val progreso: ProgressBar = binding.statPB

            val oldP = progreso.progress

            val newP = if (position == 0) 100 else (placeItem.catPercent * 100 / maxPercent).toInt()

            val aset: AnimatorSet = AnimatorSet()

            aset.playSequentially(
                ObjectAnimator.ofInt(
                    progreso,
                    "progress",
                    oldP,
                    newP * 10
                )
            )
            aset.duration = 2000
            aset.start()


            val tt = "%.1f".format(placeItem.catPercent)
            binding.precentText.text = "$tt %"
            binding.statIV.setImageResource(catIcons[placeItem.catId]!!)
        }
        binding.executePendingBindings()
    }
}
