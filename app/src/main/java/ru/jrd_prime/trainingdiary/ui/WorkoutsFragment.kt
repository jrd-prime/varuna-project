package ru.jrd_prime.trainingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.size
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.a_need_auth_page.view.*
import kotlinx.android.synthetic.main.a_workout_list_pager.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.adapter.WorkoutListAdapter
import ru.jrd_prime.trainingdiary.databinding.ANeedAuthPageBinding
import ru.jrd_prime.trainingdiary.databinding.AWorkoutListPagerNewBinding
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.utils.getDatesWeekList
import ru.jrd_prime.trainingdiary.utils.getStartDateForPosition
import ru.jrd_prime.trainingdiary.utils.getWeekFromDate
import ru.jrd_prime.trainingdiary.utils.setDateForHead


const val ARGUMENT_PAGE_NUMBER = "arg_page_number"

class WorkoutPageFragment : Fragment() {
    private var pageNumber = 0
    private val appContainerz: AppContainer by lazy {
        (activity?.application as TrainingDiaryApp).container
    }

    /*
    Метод newInstance создает новый экземпляр фрагмента и записывает ему в атрибуты число,
    которое пришло на вход. Это число – номер страницы, которую хочет показать ViewPager.
    По нему фрагмент будет определять, какое содержимое создавать в фрагменте. */
    companion object {
        fun newInstance(page: Int): WorkoutPageFragment {
            val pageFragment = WorkoutPageFragment()
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page)
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    /*
    В onCreate читаем номер страницы из аргументов.
    Далее формируем цвет из рандомных компонентов.
    Он будет использоваться для фона страниц, чтобы визуально отличать одну страницу от другой.*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = requireArguments().getInt(ARGUMENT_PAGE_NUMBER)
    }

    /*
    В onCreateView создаем View, находим на нем TextView, пишем ему простой текст с номером страницы и ставим фоновый цвет.
    Т.е. на вход у нас идет номер страницы, а на выходе получаем фрагмент, который отображает этот номер и имеет случайный фоновый цвет.*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fireBaseCore = FireBaseCore(appContainerz)
        if (!appContainerz.appUtils.getUserAuth()) {
            /* USER NOT AUTH*/
            val pagerBinding: ANeedAuthPageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_need_auth_page,
                container,
                false
            )
            val rootView = pagerBinding.root
            rootView.btnSignInOnMain.setOnClickListener {
                appContainerz.gAuth.gSignIn(activity) /*SIGN IN*/
            }
            return rootView
        } else {
            val pagerBinding: AWorkoutListPagerNewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_workout_list_pager_new,
                container,
                false
            )
            val rootView = pagerBinding.root
            val workoutsListAdapter = WorkoutListAdapter()
            workoutsListAdapter.notifyDataSetChanged()
            rootView.recView.adapter = workoutsListAdapter

            MobileAds.initialize(context)
            val mAdView = pagerBinding.adView
            val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
            mAdView.loadAd(adRequest)




            setDateForHead(view = activity?.findViewById<TextView>(R.id.tvTodayDay))

            val t = activity?.findViewById<TextView>(R.id.tvTodayDay)
            val workoutPager = activity?.findViewById<ViewPager>(R.id.viewPagerMainDashboard)
            t?.setOnClickListener {
                workoutPager?.setCurrentItem(START_PAGE, true)
            }


            val date = getWeekFromDate(getStartDateForPosition(pageNumber))
            val dates = getDatesWeekList(startDate = date[0])
//            Log.d(TAG, "onCreateView: $dates")


            val d = fireBaseCore.getWeekData(dates, workoutsListAdapter)
            FireBaseCore(appContainerz).listenNewData(workoutsListAdapter)


            rootView.recView.layoutManager = LinearLayoutManager(context)
            val scrollView = rootView.cont_layz as NestedScrollView
            scrollView.isFillViewport = true
            return rootView
        }
    }
}