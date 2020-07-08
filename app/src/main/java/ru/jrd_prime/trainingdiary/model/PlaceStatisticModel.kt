package ru.jrd_prime.trainingdiary.model

data class PlaceStatisticModel(
    val catId: Int,
    var catPercent: Float,
    var catPlace: Int = 0
) {}