package ru.jrd_prime.trainingdiary.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.a_workout_list_pager.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.adapter.WorkoutListAdapter
import ru.jrd_prime.trainingdiary.databinding.AWorkoutListPagerBinding
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.calcDateFromPosition
import ru.jrd_prime.trainingdiary.utils.dateCut
import java.util.*


const val ARGUMENT_PAGE_NUMBER = "arg_page_number"

class WorkoutPageFragment : Fragment() {
    private var pageNumber = 0
    private var backColor = 0


    /*
    Метод newInstance создает новый экземпляр фрагмента и записывает ему в атрибуты число,
    которое пришло на вход. Это число – номер страницы, которую хочет показать ViewPager.
    По нему фрагмент будет определять, какое содержимое создавать в фрагменте.
    */
    companion object {
        fun newInstance(page: Int): WorkoutPageFragment {
            val pageFragment =
                WorkoutPageFragment()
            val arguments = Bundle()

            arguments.putInt(ARGUMENT_PAGE_NUMBER, page)
            pageFragment.arguments = arguments
            return pageFragment
        }
    }

    /*
    В onCreate читаем номер страницы из аргументов.
    Далее формируем цвет из рандомных компонентов.
    Он будет использоваться для фона страниц, чтобы визуально отличать одну страницу от другой.
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageNumber = requireArguments().getInt(ARGUMENT_PAGE_NUMBER)

        Log.d("\nHERE\n", "pageNum $pageNumber")
        val rnd = Random()
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    /*
    В onCreateView создаем View, находим на нем TextView, пишем ему простой текст с номером страницы и ставим фоновый цвет.
    Т.е. на вход у нас идет номер страницы, а на выходе получаем фрагмент, который отображает этот номер и имеет случайный фоновый цвет.
*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val pagerBinding: AWorkoutListPagerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.a_workout_list_pager,
            container,
            false
        )

        val rootView = pagerBinding.root

        val data: Collection<WorkoutModel> = dateCut(calcDateFromPosition(pageNumber))
        val adapter = WorkoutListAdapter(data)

        rootView.recView.layoutManager = LinearLayoutManager(context)


        rootView.recView.adapter = adapter

//        val view: View = inflater.inflate(R.layout.a_workout_list_pager, null)
        val scrollView = rootView.cont_layz as NestedScrollView
        scrollView.isFillViewport = true


//        val tvPage = root.texttext as TextView
//        tvPage.text = "Page $pageNumber"
//        tvPage.setBackgroundColor(backColor)

//        pagerBinding.workoutCase = newWork
        return rootView
    }
}