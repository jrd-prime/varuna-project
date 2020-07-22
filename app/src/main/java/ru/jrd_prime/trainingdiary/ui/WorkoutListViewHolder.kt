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
import kotlinx.android.synthetic.main.card_extra_view.view.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.databinding.ANewCardViewBinding
import ru.jrd_prime.trainingdiary.databinding.CardExtraViewBinding
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.handlers.setGone
import ru.jrd_prime.trainingdiary.handlers.setVisible
import ru.jrd_prime.trainingdiary.utils.*
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(_binding: ANewCardViewBinding) :
    RecyclerView.ViewHolder(_binding.root) {
    private val binding: ANewCardViewBinding = _binding
    private val root: View = binding.root
    private val ctx: Context = root.context
    private val res: Resources = ctx.resources
    private val li = LayoutInflater.from(ctx)
    private val shPref =
        ctx.getSharedPreferences(AppConfig.SHARED_PREFERENCE_NAME_FOR_CARD, Context.MODE_PRIVATE)
    private val settings = AppSettingsCore(ctx)

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


        val hideView = binding.hideThis

        // set visible card
        if (shPref.getBoolean(workout?.id, false)) hideView.visibility =
            View.VISIBLE else hideView.visibility = View.GONE


        binding.todayMarker.visibility = View.GONE
        val da = DateTimeFormatter.ofPattern("dd")
        val t = LocalDateTime.now()
        val z = da.format(t)
        if (workout?.id?.split("-")?.get(2).equals(z)) {
            binding.todayMarker.visibility = View.VISIBLE
        }

        setDefaultVisibilityToViews() // Скрываем вьювы по дефаулту
        setNumOfDayToView(workout?.id) // Пишем дату во вьюв
        setWeekDayToView(position) // Пишем день недели во вьюв

        if (workout != null) {

            var totalTime = 0
            var totalCalories = 0
            var totalDistance = 0f


            val wo = workout
            val adds = workout.additional //todo if adds null - need write empty data to db

            if (!wo.empty) { /* wo NOT EMPTY */
                showMainView(workoutData = wo) // show main view with info

                totalTime += wo.time
                totalCalories += wo.kcal
                totalDistance += wo.distance

                if (adds.isNullOrEmpty()) showAddsEmptyView(
                    0,
                    wo.id
                ) else {
                    showAddsViews(adds, wo.id)

                    for (add in adds) {
                        if (!add.value.empty) {
                            totalTime += add.value.time
                            totalCalories += add.value.kcal
                            totalDistance += add.value.distance
                        }
                    }

                    binding.tvTotalMinutes.text =
                        String.format(res.getString(R.string.minutes_val), totalTime.toString())

                    binding.tvTotalCalories.text = String.format(
                        res.getString(R.string.calories_val),
                        totalCalories.toString()
                    )

                    binding.tvTotalDistance.text = String.format(
                        res.getString(R.string.distance_val),
                        totalDistance.toString()
                    )


                }
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
//        Log.d(TAG, "showAddsViews/addsData: $addsData")
        if (addsData == null) return

        var filledExtraWorkoutsSize = 0
        val allExtraWorkoutSize = addsData.size
        val sortedAddsData = addsData.toSortedMap()

        for (add in sortedAddsData) {
            if (!add.value.empty) filledExtraWorkoutsSize += 1
        }

//        Log.d(TAG, "showAddsViews/filledExtraWorkoutsSize: $filledExtraWorkoutsSize")

        when (filledExtraWorkoutsSize) {
            0 -> {
                showAddsEmptyView(0, mainWorkoutDate)

            }
            1 -> {
                for (add in sortedAddsData) {
                    if (!add.value.empty) showAddsView(0, add.key, add.value)
                }

                showAddsEmptyView(1, mainWorkoutDate)
            }
            2 -> {
                var i = 0
                for (add in sortedAddsData) {
                    if (!add.value.empty) showAddsView(i, add.key, add.value)
                    i++
                }
                showAddsEmptyView(2, mainWorkoutDate)
            }
            3 -> {
                var i = 0
                for (add in sortedAddsData) {
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
                fillLine(view = incLine1Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine1Filled.iv)
                setVisible(addsLine1)
            }
            1 -> {
                fillLine(view = incLine2Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine2Filled.iv)
                setVisible(addsLine2)
            }
            2 -> {
                fillLine(view = incLine3Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine3Filled.iv)
                setVisible(addsLine3)
            }
        }
    }

    private fun fillLine(view: CardExtraViewBinding, key: String, data: Workout) {
        // containers
        val container = view.addsCardCont
        val categoryIconView = view.iv
        // data holders
        val title = view.tvTitle
        val time = view.textTime
        // hidden
        val keyHolder = view.cardHiddenTextWithAddKey
        val idHolder = view.cardHiddenTextWithID

        if (!settings.getShowWorkoutDescription()) setGone(container.extraDesc)

        /* FILL START */
        keyHolder.text = key
        idHolder.text = data.id

        // TITLE
        if (data.category == 4) {
            if (data.title.isNotEmpty()) title.text = data.title else title.text =
                strNoTitle
        } else {
            if (data.title.isNotEmpty()) title.text = data.title else title.text = strNoTitle
        }
// TIME
        if (data.time == 0) {
            setGone(view.textTimeContainer)
        } else {

            setVisible(view.textTimeContainer)
            time.text = data.convertMinsToHM(res)
        }


        if (data.description.isNotEmpty()) {
            if (settings.getShowWorkoutDescription()) setVisible(container.extraDesc)
        } else {
            setGone(container.extraDesc)
        }

        container.tvExtraDesc.text = data.getCheckedDescription(res)

        /* END FILL */
        setImageFromConstantsWOCorners(data.category, categoryIconView, view.dotView)
        setVisible(container)
    }


    private fun showAddsEmptyView(inLine: Int, mainWorkoutDate: String) {
//        binding.additionalCardsHolder.removeViewAt(atPosition) /* remove all views to fix dublicates */
        when (inLine) {
            0 -> {
                setVisible(addsLine1)
                setVisible(incLine1Empty.addsCardCont)
                incLine1Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            1 -> {
                setVisible(addsLine2)
                setVisible(incLine2Empty.addsCardCont)
                incLine2Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            2 -> {
                setVisible(addsLine3)
                setVisible(incLine3Empty.addsCardCont)
                incLine3Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
        }
    }

    private fun showMainView(workoutData: Workout) { // show main card with workout info
        Log.d(TAG, "showMainView: ")
        val cardF = binding.incCardFilled
        val cardE = binding.incCardEmpty
        setVisible(cardF.mainCardFilled)
        setGone(cardE.mainCardEmpty)

        if (!settings.getShowWorkoutDescription()) setGone(cardF.mainDesc)

        cardF.workout = workoutData
        binding.cardId.text = workoutData.id
        val time = cardF.textTime
        val title = cardF.tvMuscleGroup

        var extraCount = 0

        val adds = workoutData.additional

        if (adds != null) {
            for (add in adds) {
                if (!add.value.empty) extraCount += 1
            }
        }

        if (extraCount != 0) {
            binding.extraCount.visibility = View.VISIBLE
            binding.extraCount.text = "+$extraCount"
        }


        if (workoutData.category == 4) {
            if (workoutData.title.isNotEmpty()) title.text =
                workoutData.title else title.text =
                strNoTitle
        } else {
            if (workoutData.title.isNotEmpty()) title.text = workoutData.title else title.text =
                strNoTitle
        }
        if (workoutData.time == 0) {
            time.visibility = View.GONE
        } else {
            time.visibility = View.VISIBLE
            time.text = minutesToHoursAndMinutes(workoutData.time, res)
        }

        if (workoutData.description.isNotEmpty()) {
            Log.d(TAG, "showMainView: ${workoutData.description}")
            cardF.tvMainDesc.text = workoutData.description
            if (settings.getShowWorkoutDescription()) cardF.mainDesc.visibility =
                View.VISIBLE
        } else {
            Log.d(TAG, "showMainView: desc null or empty")
            cardF.tvMainDesc.text = ""
            cardF.mainDesc.visibility = View.GONE
        }

        setImageFromConstantsWOCorners(
            workoutData.category,
            cardF.iv,
            cardF.dotView
        )
        setImageFromConstants(workoutData.category, cardF.iv)
        setImageFromConstants(workoutData.category, binding.ivCategory)
    }

    private fun showEmptyMainView(workoutData: Workout) { // show empty main view with add button
        binding.incCardFilled.mainCardFilled.visibility = View.GONE
        binding.incCardEmpty.mainCardEmpty.visibility = View.VISIBLE
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
        binding.extraCount.visibility = View.GONE

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
//            setImageFromConstants(catId, binding.ivCategory, binding.dot)
        } else {
            /* Категории нету */
//            setImageFromConstants(0, binding.ivCategory, binding.dot)
        }
    }

    private fun setImageFromConstants(catId: Int, catIv: ImageView, dot: FrameLayout) {
        catIv.setImageResource(catIcons[catId] as Int)
        dot.setBackgroundResource(catColor[catId] as Int)
    }

    private fun setImageFromConstantsWOCorners(catId: Int, catIv: ImageView, dot: FrameLayout) {
        catIv.setImageResource(catIcons[catId] as Int)
        dot.setBackgroundResource(catColorBGNoCorners[catId] as Int)
    }

    private fun setImageFromConstants(catId: Int, catIv: ImageView) {
        catIv.setImageResource(catIcons[catId] as Int)
//        dot.setBackgroundResource(catColor[catId] as Int)
    }
}