package ru.jrd_prime.trainingdiary.handlers

import android.util.Log
import androidx.viewpager.widget.ViewPager
import ru.jrd_prime.trainingdiary.ui.TAG

val pageListener = object : ViewPager.OnPageChangeListener {
    override fun onPageSelected(position: Int) {
        Log.d(TAG, "onPageSelected, position = $position")
    }

    override fun onPageScrolled(
        position: Int, positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    override fun onPageScrollStateChanged(state: Int) {}
}