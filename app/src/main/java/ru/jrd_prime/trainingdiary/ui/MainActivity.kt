package ru.jrd_prime.trainingdiary.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ru.jrd_prime.trainingdiary.PageFragment
import ru.jrd_prime.trainingdiary.R

const val TAG = "myLogs"
const val PAGE_COUNT = 10

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout._root_layout)
//    }


    var pager: ViewPager? = null
    var pagerAdapter: PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout._root_layout)



        pager = findViewById<ViewPager>(R.id.viewPagerMainDashboard)
        pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        pager!!.adapter = pagerAdapter
        pager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                Log.d(TAG, "onPageSelected, position = $position")
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private class MyFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {

            return PageFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return PAGE_COUNT
        }
    }
}