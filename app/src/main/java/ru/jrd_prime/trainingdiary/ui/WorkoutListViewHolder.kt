package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_extra_empty_view.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.ANewCardViewBinding
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.catColor
import ru.jrd_prime.trainingdiary.utils.catIcons

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(_binding: ANewCardViewBinding) :
    RecyclerView.ViewHolder(_binding.root) {
    private val binding: ANewCardViewBinding = _binding
    private val root: View = binding.root
    private val ctx: Context = root.context
    private val res: Resources = ctx.resources
    private val li = LayoutInflater.from(ctx)

    // clr
    private val clrLightForText = ContextCompat.getColor(ctx, R.color.colorForLightText)
    private val clrDarkGrey = ContextCompat.getColor(ctx, R.color.colorGreyDark)

    // str
    private val strNoTitle = res.getString(R.string.no_title)
    private val strNoDesc = res.getString(R.string.no_description)

    // containers
    private val addsLine1 = binding.addsCardLine1
    private val addsLine2 = binding.addsCardLine2
    private val addsLine3 = binding.addsCardLine3
    private var incLine1Filled = binding.incAddsCardFilled1
    private var incLine2Filled = binding.incAddsCardFilled2
    private var incLine3Filled = binding.incAddsCardFilled3
    private var incLine1Empty = binding.incAddsCardEmpty1
    private var incLine2Empty = binding.incAddsCardEmpty2
    private var incLine3Empty = binding.incAddsCardEmpty3

    fun bind(workout: Workout?, position: Int) {
        setDefaultVisibilityToViews() // Скрываем вьювы по дефаулту
        setNumOfDayToView(workout?.id) // Пишем дату во вьюв
        setWeekDayToView(position) // Пишем день недели во вьюв

        if (workout != null) {
            val wo = workout
            val adds = workout.additional //todo if adds null - need write empty data to db

            if (!wo.empty) { /* wo NOT EMPTY */
                showMainView(workoutData = wo) // show main view with info
                if (adds.isNullOrEmpty()) showAddsEmptyView(
                    0,
                    wo.id
                ) else showAddsViews(adds, wo.id)
            } else { /* wo EMPTY*/
                showEmptyMainView(wo)
            }

        } else {
            Log.d(TAG, "workout = null")
        }
//            steCategoryImage(workout.category)

        binding.workoutModel = workout // Отправляем кейс в карточку
//        }
        binding.executePendingBindings()
    }


    private fun showAddsViews(addsData: HashMap<String, Workout>?, mainWorkoutDate: String) {
//        addsData.removeAt(0) // remove null value
        Log.d(TAG, "showAddsViews/addsData: $addsData")
        if (addsData == null) return

        val allExtraWorkoutSize = addsData.size
        var filledExtraWorkoutsSize = 0


        for (add in addsData) {
            if (!add.value.empty) filledExtraWorkoutsSize += 1
        }

        Log.d(TAG, "showAddsViews/filledExtraWorkoutsSize: $filledExtraWorkoutsSize")

        when (filledExtraWorkoutsSize) {
            0 -> {
                showAddsEmptyView(0, mainWorkoutDate)

            }
            1 -> {
                for (add in addsData) {
                    if (!add.value.empty) showAddsView(0, add.key, add.value)
                }

                showAddsEmptyView(1, mainWorkoutDate)
            }
            2 -> {
                var i = 0
                for (add in addsData) {
                    if (!add.value.empty) showAddsView(i, add.key, add.value)
                    i++
                }
                showAddsEmptyView(2, mainWorkoutDate)
            }
            3 -> {
                var i = 0
                for (add in addsData) {
                    if (!add.value.empty) showAddsView(i, add.key, add.value)
                    i++
                }
            }
        }
    }

    private fun showAddsView(
        inLine: Int,
        key: String,
        extraWorkout: Workout
    ) {
        when (inLine) {
            0 -> {
                addsLine1.visibility = View.VISIBLE
                incLine1Filled.addsCardCont.visibility = View.VISIBLE
                incLine1Filled.cardHiddenTextWithAddKey.text = key
                incLine1Filled.tvMuscleGroup.text = extraWorkout.title
            }
            1 -> {
                addsLine2.visibility = View.VISIBLE
                incLine2Filled.addsCardCont.visibility = View.VISIBLE
                incLine2Filled.cardHiddenTextWithAddKey.text = key
                incLine2Filled.tvMuscleGroup.text = extraWorkout.title
            }
            2 -> {
                addsLine3.visibility = View.VISIBLE
                incLine3Filled.addsCardCont.visibility = View.VISIBLE
                incLine3Filled.cardHiddenTextWithAddKey.text = key
                incLine3Filled.tvMuscleGroup.text = extraWorkout.title
            }
        }
    }


    private fun showAddsEmptyView(inLine: Int, mainWorkoutDate: String) {
//        binding.additionalCardsHolder.removeViewAt(atPosition) /* remove all views to fix dublicates */
        when (inLine) {
            0 -> {
                addsLine1.visibility = View.VISIBLE
                incLine1Empty.addsCardCont.visibility = View.VISIBLE
                incLine1Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            1 -> {
                addsLine2.visibility = View.VISIBLE
                incLine2Empty.addsCardCont.visibility = View.VISIBLE
                incLine2Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            2 -> {
                addsLine3.visibility = View.VISIBLE
                incLine3Empty.addsCardCont.visibility = View.VISIBLE
                incLine3Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
        }
    }

    private fun showMainView(workoutData: Workout) { // show main card with workout info
        Log.d(TAG, "showMainView: ")
        binding.incCardFilled.mainCardFilled.visibility = View.VISIBLE
        binding.incCardEmpty.mainCardEmpty.visibility = View.GONE
    }

    private fun showEmptyMainView(workoutData: Workout) { // show empty main view with add button
        binding.incCardFilled.mainCardFilled.visibility = View.GONE
        binding.incCardEmpty.mainCardEmpty.visibility = View.VISIBLE
        binding.incCardEmpty.asdasd.text = workoutData.id
    }

    private fun showEmptyAdditionalPanelForId(panelId: Int) {
        Log.d(TAG, "showEmptyAdditionalPanelForId(panelId = $panelId)")
    }

    private fun showAdditionalPanels(panelsCount: Int) {
        Log.d(TAG, "showAdditionalPanels(panelsCount = $panelsCount)")

    }

    private fun setNumOfDayToView(workoutDate: String?) {
        var dateDay = "Err"
        if (!workoutDate.isNullOrEmpty()) {
            dateDay = workoutDate.split("-")[2].toString()
        } else {
            Log.d(TAG, "bind: ERROR: NULL DATE OR WORKOUT") //todo что делать?
        }
        binding.ivMonthDay.text = dateDay// Устанавливаем день недели
    }

    private fun setWeekDayToView(dayNum: Int) {
        binding.ivWeekDay.text = res.getStringArray(R.array.weekDays)[dayNum] // Устанавливаем дату
    }


    private fun setDefaultVisibilityToViews() {
        binding.incCardFilled.mainCardFilled.visibility = View.GONE
        binding.incCardEmpty.mainCardEmpty.visibility = View.GONE
        addsLine1.visibility = View.GONE
        addsLine2.visibility = View.GONE
        addsLine3.visibility = View.GONE
        incLine1Filled.addsCardCont.visibility = View.GONE
        incLine2Filled.addsCardCont.visibility = View.GONE
        incLine3Filled.addsCardCont.visibility = View.GONE
        incLine1Empty.addsCardCont.visibility = View.GONE
        incLine2Empty.addsCardCont.visibility = View.GONE
        incLine3Empty.addsCardCont.visibility = View.GONE
    }
//    private fun showClassicView(
//        workout: Workout
//    ) {
//        val cat = workout.category
//        val title = workout.title
//        val desc = workout.description
//        val time = workout.time
//
//        binding.tvMuscleGroup.setTextColor(clrDarkGrey)
//        binding.textDescription.setTextColor(clrDarkGrey)
//        binding.textTime.setTextColor(clrDarkGrey)
//        show(binding.textDescription)
//        show(binding.timeContainer)
//        show(binding.textTime)
//
//        if (cat == 4) { // if REST
//            if (desc.isEmpty()) { // desc empty
//                workout.title = strNoDesc
//                binding.tvMuscleGroup.setTextColor(clrLightForText)
//                hide(binding.textDescription)
//                hide(binding.timeContainer)
//            } else { // desc OK
//                workout.title = desc
//                hide(binding.timeContainer)
//                hide(binding.textDescription)
//            }
//        } else if (cat == 0) { // if EMPTY
//            lg("showClassicView", "EMPTY")
//        } else { // ELSE
//            if (title.isEmpty()) {
//                workout.title = strNoTitle
//                binding.tvMuscleGroup.setTextColor(clrLightForText)
//            }
//            if (desc.isEmpty()) {
//                workout.description = strNoDesc
//                binding.textDescription.setTextColor(clrLightForText)
//            }
//            if (time == 0) {
//                hide(binding.textTime)
//            } else {
//                binding.textTime.text = minutesToHoursAndMinutes(time, res)
//                show(binding.timeContainer)
//            }
//        }
//    }

    private fun showEmptyView() {
//        show(binding.cardOverLay)
    }

    private fun hide(view: View) {
        view.visibility = View.GONE
    }

    private fun show(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun steCategoryImage(catId: Int) {
        if (catId != 0) {
            /* Категория есть */
            setImageFromConstants(catId, binding.ivCategory, binding.dot)
        } else {
            /* Категории нету */
            setImageFromConstants(0, binding.ivCategory, binding.dot)
        }
    }

    private fun setImageFromConstants(catId: Int, catIv: ImageView, dot: FrameLayout) {
        catIv.setImageResource(catIcons[catId] as Int)
        dot.setBackgroundResource(catColor[catId] as Int)
    }
}