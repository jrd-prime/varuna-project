package ru.jrd_prime.trainingdiary.viewmodels

import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    val elevation: String = "50dp"
    fun getEle(): String {
        return elevation.toString()
    }
}