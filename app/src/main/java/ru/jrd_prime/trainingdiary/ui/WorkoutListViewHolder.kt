package ru.jrd_prime.trainingdiary.ui

import android.content.Context
import android.content.res.Resources
import android.util.Log
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
import ru.jrd_prime.trainingdiary.handlers.setVisbl
import ru.jrd_prime.trainingdiary.utils.AppSettingsCore
import ru.jrd_prime.trainingdiary.utils.catColor
import ru.jrd_prime.trainingdiary.utils.catColorBGNoCorners
import ru.jrd_prime.trainingdiary.utils.catIcons
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig

/* Настраиваем КАРТОЧКУ*/
class WorkoutListViewHolder(_binding: ANewCardViewBinding) :
    RecyclerView.ViewHolder(_binding.root) {
    private val binding: ANewCardViewBinding = _binding
    private val root: View = binding.root
    private val ctx: Context = root.context
    private val res: Resources = ctx.resources
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


            setImageFromConstants(workout.category, binding.ivCategory)
            var totalTime = 0
            var totalCalories = 0
            var totalDistance = 0f


            val wo = workout
            val adds = workout.additional //todo if adds null - need write empty data to db

            if (!wo.empty) { /* wo NOT EMPTY */
                showMainView(wo = wo) // show main view with info


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
            } else { /* wo EMPTY*/
                showEmptyMainView(wo)
                setGone(hideView)
            }

        } else {
            Log.d(TAG, "workout = null")
        }
        binding.workoutModel = workout // Отправляем кейс в карточку
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
                setVisbl(addsLine1)
            }
            1 -> {
                fillLine(view = incLine2Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine2Filled.iv)
                setVisbl(addsLine2)
            }
            2 -> {
                fillLine(view = incLine3Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine3Filled.iv)
                setVisbl(addsLine3)
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
            view.textTime.visibility = View.GONE
        } else {

            view.textTime.visibility = View.VISIBLE
            time.text = data.convertMinsToHM(res)
        }


        if (data.description.isNotEmpty()) {
            if (settings.getShowWorkoutDescription()) setVisbl(container.extraDesc)
        } else {
            setGone(container.extraDesc)
        }

        container.tvExtraDesc.text = data.getCheckedDescription(res)

        /* END FILL */
        setImageFromConstantsWOCorners(data.category, categoryIconView, view.dotView)
        setVisbl(container)
    }


    private fun showAddsEmptyView(inLine: Int, mainWorkoutDate: String) {
//        binding.additionalCardsHolder.removeViewAt(atPosition) /* remove all views to fix dublicates */
        when (inLine) {
            0 -> {
                setVisbl(addsLine1)
                setVisbl(incLine1Empty.addsCardCont)
                incLine1Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            1 -> {
                setVisbl(addsLine2)
                setVisbl(incLine2Empty.addsCardCont)
                incLine2Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
            2 -> {
                setVisbl(addsLine3)
                setVisbl(incLine3Empty.addsCardCont)
                incLine3Empty.cardHiddenTextWithID.text = mainWorkoutDate
            }
        }
    }

    private fun showMainView(wo: Workout) { // show main card with workout info
        val cardF = binding.incCardFilled
        val cardE = binding.incCardEmpty
        setVisbl(cardF.mainCardFilled)
        setGone(cardE.mainCardEmpty)

        if (!settings.getShowWorkoutDescription()) setGone(cardF.mainDesc)

        cardF.workout = wo
        binding.cardId.text = wo.id
        val time = cardF.textTime
        val title = cardF.tvMuscleGroup

        var extraCount = 0

        val adds = wo.additional
        if (adds != null) {
            for (add in adds) {
                if (!add.value.empty) extraCount += 1
            }
        }

        if (extraCount != 0) {
            binding.extraCount.text = "+$extraCount"
            setVisbl(binding.extraCountContainer)
        }


        if (wo.category == 4) {
            if (wo.title.isNotEmpty()) title.text =
                wo.title else title.text =
                strNoTitle
        } else {
            if (wo.title.isNotEmpty()) title.text = wo.title else title.text =
                strNoTitle
        }
        if (wo.time == 0) {
            setGone(cardF.textTimeContainer)
        } else {
            setVisbl(cardF.textTimeContainer)
            time.text = wo.convertMinsToHM(res)
        }

        if (wo.description.isNotEmpty()) {
            if (settings.getShowWorkoutDescription()) setVisbl(cardF.mainDesc)
        } else {
            setGone(cardF.mainDesc)
        }
        cardF.tvMainDesc.text = wo.getCheckedDescription(res)
        setImageFromConstantsWOCorners(
            wo.category,
            cardF.iv,
            cardF.dotView
        )
        setImageFromConstants(wo.category, cardF.iv)
        setImageFromConstants(wo.category, binding.ivCategory)
    }

    private fun showEmptyMainView(wo: Workout) { // show empty main view with add button
        setGone(binding.incCardFilled.mainCardFilled)
        setVisbl(binding.incCardEmpty.mainCardEmpty)
    }

    private fun setNumOfDayToView(wo: String?) {
        var dateDay = "Err"
        if (!wo.isNullOrEmpty()) {
            dateDay = wo.split("-")[2].toString()
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
        binding.extraCountContainer.visibility = View.GONE
        binding.tvTotalMinutes.text =
            String.format(res.getString(R.string.minutes_val), 0)

        binding.tvTotalCalories.text = String.format(
            res.getString(R.string.calories_val), 0
        )

        binding.tvTotalDistance.text = String.format(
            res.getString(R.string.distance_val), 0
        )
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
    }
}