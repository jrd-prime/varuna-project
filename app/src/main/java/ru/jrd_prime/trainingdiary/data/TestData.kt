package ru.jrd_prime.trainingdiary.data

import android.util.Log
import ru.jrd_prime.trainingdiary.model.Category
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import java.text.SimpleDateFormat
import java.util.*

fun prepData(): Collection<WorkoutModel> {
    val mlist = listOf<String>(
        "Грудь",
        "Бицепс",
        "Нижний пресс",
        "Плечи"
    )

    val mlistl = listOf<String>(
        "LongList",
        "Mock",
        "Грудь",
        "Бицепс",
        "Нижний пресс",
        "Плечи"
    )
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val currentDate: String = sdf.format(Date())

    val list = mutableListOf<WorkoutModel>()
    list.add(
        WorkoutModel(
            32,
            Category.Cardio,
            mlistl,
            "2 гантели 8п, Бицепс 15п, Попа 10п",
            32 + 2 * 3,
            currentDate
        )
    )
    list.add(
        WorkoutModel(
            31,
            Category.Rest,
            mlistl,
            "2 гантели 8п, Бицепс 15п, Попа 10п",
            32 + 2 * 3,
            currentDate
        )
    )
    Log.d("TAG", "prepData:")
    for (i in 1..7) {
        list.add(
            WorkoutModel(
                i,
                Category.Power,
                mlist,
                "2 гантели 8п, Бицепс 15п, Попа 10п",
                i + 2 * 3,
                currentDate
            )
        )
    }

    return list
}