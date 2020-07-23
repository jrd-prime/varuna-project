package ru.jrd_prime.trainingdiary.handlers

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.a_new_card_view.view.*
import kotlinx.android.synthetic.main.card_extra_empty_view.view.cardHiddenTextWithID
import kotlinx.android.synthetic.main.card_extra_view.view.*
import kotlinx.android.synthetic.main.pop_up_delete_dialog.view.*
import kotlinx.android.synthetic.main.pop_up_edit.view.*
import kotlinx.android.synthetic.main.pop_up_edit.view.textTitle
import kotlinx.android.synthetic.main.pop_up_info.view.*
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.TrainingDiaryApp
import ru.jrd_prime.trainingdiary.fb_core.FireBaseCore
import ru.jrd_prime.trainingdiary.fb_core.models.Workout
import ru.jrd_prime.trainingdiary.utils.AppSettingsCore
import ru.jrd_prime.trainingdiary.utils.cfg.AppConfig
import ru.jrd_prime.trainingdiary.utils.getPopUpView
import ru.jrd_prime.trainingdiary.utils.getPopUpWindow
import ru.jrd_prime.trainingdiary.workers.AsyncRequests


class WorkoutCardHandler(root: View) {
    private val appContainer = (root.context.applicationContext as TrainingDiaryApp).container
    private val asyncReq: AsyncRequests = AsyncRequests(appContainer)
    private val ctx: Context = root.context
    private val fbc: FireBaseCore = FireBaseCore(appContainer)
    private val asc: AppSettingsCore = AppSettingsCore(ctx)

    companion object {
        const val TAG = "Handler"
    }

    /* SHOW MAIN */
    fun showMainWorkoutInfo(root: View, workoutID: String) {
        val view: View = getPopUpView(root.context, R.layout.pop_up_info)
        val window = getPopUpWindow(view)
        val act = appContainer.activity
        val a = act as FragmentActivity

        fbc.getWorkout(object : GetWorkoutCallback {
            override fun onCallBack(workout: Workout, workoutID: String) {
                putDataToInfoUI(workout, view)
            }
        }, workoutID)

        view.btnDelete.setOnClickListener { _ ->
            Log.d(TAG, "DELETE MAIN $workoutID")
            showMainDeleteWindow(root, workoutID)
            window.dismiss()
            //todo мб сделать диалог подтверждения удаления
        }
        view.btnClose.setOnClickListener { _ -> window.dismiss() }
        view.btnEdit.setOnClickListener { _ ->
            workoutEdit(root, workoutID)
            window.dismiss()
        }

        root.post(Runnable { window.showAtLocation(root, Gravity.BOTTOM, 0, 0) })
    }

    private fun showMainDeleteWindow(root: View, workoutID: String) {
        val view = getPopUpView(root.context, R.layout.pop_up_delete_dialog)
        val window = getPopUpWindow(view)
        view.btnDelete_DelDialog.setOnClickListener { _ ->
            deleteMainWorkout(workoutID)
            window.dismiss()
        }
        view.btnClose_DelDialog.setOnClickListener { _ -> window.dismiss() }
        view.btnCancel_DelDialog.setOnClickListener { _ ->
            showMainWorkoutInfo(root, workoutID)
            window.dismiss()
        }
//todo создать чекбокс для ускорения удаления. типа не спрашивать больше удалять
        root.post(Runnable { window.showAtLocation(root, Gravity.BOTTOM, 0, 0) })
    }

    private fun deleteMainWorkout(woID: String) {

        Log.d(TAG, "DELETE MAIN $woID")
        fbc.deleteMainWorkout(woID)
    }

    /* CREATE AND UPDATE MAIN */
    fun addMainWorkout(view: View, workoutDate: String) {
        Log.d(CardHandler.TAG, "add main workout: $workoutDate")

        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop_up_edit, null)
        popupView.textTitle.setText(R.string.add_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        setCategoryListeners(popupView)
        popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        popupView.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(popupView, workoutDate)
            FireBaseCore(appContainer).updateWorkout(workoutDate, dataFromUI)

            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }

    /* SHOW ALL */
    fun showAll(view: View, workoutID: String) {
        Log.d(TAG, "showAll: id: $workoutID")
    }


    /* EDIT MAIN */
    fun workoutEdit(view: View, workoutID: String) {

        Log.d(TAG, "workoutEdit: $workoutID")
        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop_up_edit, null)
        popupView.textTitle.setText(R.string.edit_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )

        FireBaseCore(appContainer).getWorkout(object : GetWorkoutCallback {
            override fun onCallBack(workout: Workout, workoutID: String) {
                val wo: Workout = workout
                Log.d(TAG, "onCallBack: $wo")

                putDataToUI(wo, popupView)

            }
        }, workoutID)

        setCategoryListeners(popupView)
        popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        popupView.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(popupView, workoutID)
            Log.d(TAG, "workoutEdit: collected data : ${dataFromUI.toString()}")
            FireBaseCore(appContainer).updateWorkout(workoutID, dataFromUI)
            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        view.post(Runnable {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        })
    }

    /* DELETE MAIN */
    fun clearWorkout(view: View, workoutID: String) {
        Log.d(TAG, "clearWorkout: $workoutID")
    }

    /* CREATE AND UPDATE EXTRA */
    fun addExtraWorkout(view: View) {
        val workoutDate = view.cardHiddenTextWithID.text.toString()
        Log.d(CardHandler.TAG, "addAddsWorkout : $workoutDate")

        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop_up_edit, null)
        popupView.textTitle.setText(R.string.add_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        setCategoryListeners(popupView)
        popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        popupView.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(popupView, workoutDate)
            fbc.addMoreWorkout(workoutDate, dataFromUI)

            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        view.post(Runnable {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        })
    }

    /* SHOW EXTRA INFO */


    fun showExtraWorkoutInfo(root: View) {
        val workoutID = root.cardHiddenTextWithID.text.toString()
        val key = root.cardHiddenTextWithAddKey.text.toString()

        val view = getPopUpView(root.context, R.layout.pop_up_info)
        val window = getPopUpWindow(view)

        fbc.getExtraWorkout(object : GetWorkoutCallback {
            override fun onCallBack(workout: Workout, workoutID: String) {
                Log.d(TAG, "onCallBack: ")
                putDataToInfoUI(workout, view)
            }
        }, workoutID, key)

        view.btnDelete.setOnClickListener { _ ->
            Log.d(TAG, "DELETE EXTRA $key")
            fbc.deleteExtraWorkout(workoutID, key)
            window.dismiss()
        }
        view.btnClose.setOnClickListener { _ -> window.dismiss() }
        view.btnEdit.setOnClickListener { _ ->
            editExtraWorkout(root)
            window.dismiss()
        }

        root.post(Runnable {
            window.showAtLocation(view, Gravity.BOTTOM, 0, 0)
        })
    }

    /* EDIT EXTRA */
    private fun editExtraWorkout(root: View) {
        val workoutID = root.cardHiddenTextWithID.text.toString()
        val key = root.cardHiddenTextWithAddKey.text.toString()

        val view: View = getPopUpView(root.context, R.layout.pop_up_edit)
        val window = getPopUpWindow(view)
        view.textTitle.setText(R.string.edit_dialog_title)


        fbc.getExtraWorkout(object : GetWorkoutCallback {
            override fun onCallBack(workout: Workout, workoutID: String) {
                val wo: Workout = workout
                Log.d(TAG, "onCallBack: $wo")

                putDataToUI(wo, view)

            }
        }, workoutID, key)

        setCategoryListeners(view)
        view.btnCancel.setOnClickListener { _ -> window.dismiss() }
        view.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(view, workoutID)
            fbc.updateExtraWorkout(workoutID, dataFromUI, key)
            window.dismiss()
        }

        root.post(Runnable {
            window.showAtLocation(root, Gravity.BOTTOM, 0, 0)
        })
    }

    /* DELETE EXTRA */
    fun deleteExtraWorkout(view: View) {
        Log.d(
            TAG,
            "deleteExtraWorkout: key:${view.cardHiddenTextWithAddKey.text} id:${view.cardHiddenTextWithID.text}"
        )
    }


    fun workoutDelete(view: View, workoutID: String) {
//        asyncReq.clearWorkout(workoutID) /* Set workout empty = true */
        FireBaseCore(appContainer).clearWorkout(workoutID)
        val snack = Snackbar.make(view, R.string.snack_record_deleted, 7000)
        val snackView = snack.view
        snack.setAction(R.string.snack_restore) {
//            asyncReq.restoreWorkout(workoutID) /* Set workout empty = false */
            FireBaseCore(appContainer).restoreWorkout(workoutID)
        }
        snack.setActionTextColor(ctx.getColor(R.color.colorSnackbarButton))
        snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ctx.getColor(R.color.colorLightGrey))
        snackView.setBackgroundResource(R.drawable.snack_bg)
        snack.show()
    }


    fun workoutAdd(view: View, workoutID: String) {
        Log.d(TAG, "workoutAdd: $workoutID")

        val popupView: View =
            LayoutInflater.from(view.context).inflate(R.layout.pop_up_edit, null)
        popupView.textTitle.setText(R.string.add_dialog_title)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        setCategoryListeners(popupView)
        popupView.btnCancel.setOnClickListener { _ -> popupWindow.dismiss() }
        popupView.btnSave.setOnClickListener { _ ->
            val dataFromUI = collectDataFromUI(popupView, workoutID)
            asyncReq.updateWorkout(workoutID, dataFromUI)

            popupWindow.dismiss()
        }
        popupWindow.elevation = 20f
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
    }


    fun showAdditionalInfoNew(view: View?) {
        val shPref = ctx.getSharedPreferences(
            AppConfig.SHARED_PREFERENCE_NAME_FOR_CARD,
            Context.MODE_PRIVATE
        )
        var rotationAngle = 0f
        rotationAngle = if (rotationAngle == 0f) 180f else 0f //toggle
//todo починить поворот
        if (view == null) return else {
            val cardID = view.cardId.text.toString()
            view.ivOpener.animate().rotation(rotationAngle).setDuration(500).start()
            val cardView = view.parent.parent.parent.parent as View
            val contView = cardView.hideThis
            when (contView.visibility) {
                View.GONE -> {
                    setVisible(contView)
                    shPref.edit().putBoolean(cardID, true).apply()
                }
                View.VISIBLE -> {
                    setGone(contView)
                    shPref.edit().putBoolean(cardID, false).apply()

                }
            }
        }
    }

    /*DEL*/
    fun showAdditionalInfo(view: View?) {
        if (view == null) return
//        val contView = view.frameForHide

//        when (contView.visibility) {
//            View.GONE -> setVisible(contView)
//            View.VISIBLE -> setGone(contView)
//        }
    }
}