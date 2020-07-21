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
                addsLine1.visibility = View.VISIBLE
            }
            1 -> {
                fillLine(view = incLine2Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine2Filled.iv)
                addsLine2.visibility = View.VISIBLE
            }
            2 -> {
                fillLine(view = incLine3Filled, key = key, data = extraWorkout)
                setImageFromConstants(extraWorkout.category, incLine3Filled.iv)
                addsLine3.visibility = View.VISIBLE
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

        if (!settings.getShowWorkoutDescription()) container.extraDesc.visibility = View.GONE

        /* FILL START */
        keyHolder.text = key
        idHolder.text = data.id

        // TITLE
        if (data.category == 4) {
            if (!data.description.isNullOrEmpty()) title.text = data.description else title.text =
                strNoDesc
        } else {
            if (!data.title.isNullOrEmpty()) title.text = data.title else title.text = strNoTitle
        }
// TIME
        if (data.time == 0) {
            time.visibility = View.GONE
        } else {
            time.visibility = View.VISIBLE
            time.text = minutesToHoursAndMinutes(data.time, res)
        }


        if (!data.description.isNullOrEmpty()) {
            container.tvExtraDesc.text = data.description
            if (settings.getShowWorkoutDescription()) container.extraDesc.visibility = View.VISIBLE
        }

        /* END FILL */
        setImageFromConstantsWOCorners(data.category, categoryIconView, view.dotView)
        container.visibility = View.VISIBLE
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


        if (!settings.getShowWorkoutDescription()) binding.incCardFilled.mainDesc.visibility = View.GONE

        binding.incCardFilled.workout = workoutData
        binding.cardId.text = workoutData.id
        val time = binding.incCardFilled.textTime
        val title = binding.incCardFilled.tvMuscleGroup

        var extraCount = 0

        val adds = workoutData.additional

        if (adds != null) {
            for (add in adds) {
                if (!add.value.empty) extraCount += 1
            }
        }

        if (extraCount != 0) {
            binding.extraCount.visibility = View.VISIBLE
            binding.extraCount.text = "+ $extraCount"
        }


        if (workoutData.category == 4) {
            if (!workoutData.description.isNullOrEmpty()) title.text =
                workoutData.description else title.text =
                strNoDesc
        } else {
            if (!workoutData.title.isNullOrEmpty()) title.text = workoutData.title else title.text =
                strNoTitle
        }
        if (workoutData.time == 0) {
            time.visibility = View.GONE
        } else {
            time.visibility = View.VISIBLE
            time.text = minutesToHoursAndMinutes(workoutData.time, res)
        }

        if (!workoutData.description.isNullOrEmpty()) {
            binding.incCardFilled.tvMainDesc.text = workoutData.description
            if (settings.getShowWorkoutDescription()) binding.incCardFilled.mainDesc.visibility =
                View.VISIBLE
        }

        setImageFromConstantsWOCorners(
            workoutData.category,
            binding.incCardFilled.iv,
            binding.incCardFilled.dotView
        )
        setImageFromConstants(workoutData.category, binding.incCardFilled.iv)
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