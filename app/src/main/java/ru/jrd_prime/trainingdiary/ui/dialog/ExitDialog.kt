package ru.jrd_prime.trainingdiary.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import ru.jrd_prime.trainingdiary.R
import ru.jrd_prime.trainingdiary.ui.DashboardActivity
import ru.jrd_prime.trainingdiary.ui.TAG
import kotlin.system.exitProcess


class ExitDialog : DialogFragment(), View.OnClickListener {
    val LOG_TAG = "myLogs"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setTitle("Close APP ???")
        val v: View = inflater.inflate(R.layout.dialog, null)
        v.findViewById<Button>(R.id.btnYes).setOnClickListener(this)
        v.findViewById<Button>(R.id.btnNo).setOnClickListener(this)
        v.findViewById<Button>(R.id.btnMaybe).setOnClickListener(this)
        return v
    }

    override fun onClick(v: View) {
        Log.d(LOG_TAG, "Dialog 1: " + (v as Button).getText())
        when (v.id) {
            R.id.btnYes -> {
                Log.d(TAG, "onOptionsItemSelected: close")
                ActivityCompat.finishAffinity(DashboardActivity())
                exitProcess(0)
            }
            R.id.btnNo -> {
                Log.d(TAG, "but neg: ")
            }
            R.id.btnMaybe -> {
                Log.d(TAG, "but ney: ")
            }
        }
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {

        super.onDismiss(dialog)

        Log.d(LOG_TAG, "Dialog 1: onDismiss")
    }

    override fun onCancel(dialog: DialogInterface) {

        super.onCancel(dialog)

        Log.d(LOG_TAG, "Dialog 1: onCancel")
    }

}