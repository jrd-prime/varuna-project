package ru.jrd_prime.trainingdiary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.jrd_prime.trainingdiary.ui.PAGE_COUNT
import ru.jrd_prime.trainingdiary.ui.WorkoutPageFragment

class WorkoutPageAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return WorkoutPageFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }
}

//class WorkoutPageAdapter(
//    fm: FragmentManager?
//) : FragmentPagerAdapter(fm!!) {
//    override fun getItem(position: Int): Fragment {
//        return WorkoutPageFragment.newInstance(position)
//    }
//
//    override fun getCount(): Int {
//        return PAGE_COUNT
//    }
//}