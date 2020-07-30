package ru.jrd_prime.trainingdiary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
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

const val ARGUMENT_PAGE_NUMBER = "arg_page_number"
const val ARGUMENT_USER_AUTH = "arg_user_auth"
const val ARGUMENT_USER_PREMIUM = "arg_user_premium"

class WorkoutPageFragment : Fragment() {
    private var pageNumber = 0
    private val appCont: AppContainer by lazy {
        (activity?.application as TrainingDiaryApp).container
    }

    /*
    Метод newInstance создает новый экземпляр фрагмента и записывает ему в атрибуты число,
    которое пришло на вход. Это число – номер страницы, которую хочет показать ViewPager.
    По нему фрагмент будет определять, какое содержимое создавать в фрагменте. */
    companion object {
        const val TAG = "WorkoutsFragment: drops"
        fun newInstance(page: Int): WorkoutPageFragment {
            val pageFragment = WorkoutPageFragment()
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PAGE_NUMBER, page)
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = requireArguments().getInt(ARGUMENT_PAGE_NUMBER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fbc = FireBaseCore(appCont)
        if (!appCont.preferences.getBoolean(appCont.appConfig.getPrefIsUserAuth(), false)) {
            /* USER NOT AUTH*/
            val binding: ANeedAuthPageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_need_auth_page,
                container,
                false
            )
            val root = binding.root
            root.btnSignInOnMain.setOnClickListener {
                appCont.gAuth.gSignIn(activity) /*SIGN IN*/
            }
            return root
        } else {
            val binding: AWorkoutListPagerNewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.a_workout_list_pager_new,
                container,
                false
            )
            val root = binding.root
            val workoutsListAdapter = WorkoutListAdapter()
            workoutsListAdapter.notifyDataSetChanged()
            root.recView.adapter = workoutsListAdapter

            MobileAds.initialize(context)
            val mAdView = binding.adView
            val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
            mAdView.loadAd(adRequest)


            val t = activity?.findViewById<TextView>(R.id.tvTodayDay)
            val workoutPager = activity?.findViewById<ViewPager>(R.id.viewPagerMainDashboard)
            t?.setOnClickListener {
                workoutPager?.setCurrentItem(START_PAGE, true)
            }


            val date = getWeekFromDate(getStartDateForPosition(pageNumber))
            val dates = getDatesWeekList(startDate = date[0])

            fbc.getWeekData(dates, workoutsListAdapter)
            fbc.listenNewData(workoutsListAdapter)


            root.recView.layoutManager = LinearLayoutManager(context)
            val scrollView = root.cont_layz as NestedScrollView
            scrollView.isFillViewport = true
            return root
        }
    }
}