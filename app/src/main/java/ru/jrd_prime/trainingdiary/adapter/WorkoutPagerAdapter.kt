package ru.jrd_prime.trainingdiary.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jrd_prime.trainingdiary.ui.PAGE_COUNT
import ru.jrd_prime.trainingdiary.ui.WorkoutPageFragment

class WorkoutPageAdapter(
    manager: FragmentManager,
    private val userAuth: Boolean,
    private val userPremium: Boolean
) :
    FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        Log.d("drops", "getItem: $userAuth")
        return WorkoutPageFragment.newInstance(position, userAuth, userPremium)
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }
}