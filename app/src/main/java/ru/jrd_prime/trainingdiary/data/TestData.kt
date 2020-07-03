package ru.jrd_prime.trainingdiary.data

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.jrd_prime.trainingdiary.impl.AppContainer
import ru.jrd_prime.trainingdiary.model.WorkoutModel
import ru.jrd_prime.trainingdiary.utils.getDatesBetweenUsingJava7
import java.text.DateFormat
import java.text.DateFormat.LONG
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

    var cal = Calendar.getInstance()
    var dat: Date = cal.time


    val datf = DateFormat.getDateInstance()

    val myString = DateFormat.getDateInstance(LONG).format(dat)
    var zzz =
        "LongList, " +
                "Mock, " +
                "Грудь, " +
                "Бицепс, " +
                "Нижний пресс, " +
                "Плечи"



//    Log.d("TAG", "prepData: $myString")
    val list = mutableListOf<WorkoutModel>()


    val startDay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = "01-06-2020"
    val startDayDate: Date = sdf.parse(dateInString)
    val endDay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString2 = "25-07-2020"
    val endDayDate: Date = sdf.parse(dateInString2)
    val dates =
        getDatesBetweenUsingJava7(startDayDate, endDayDate)

    var id = 1
//    for (i in dates) {
//
//        list.add(
//            WorkoutModel(
//                id,
////                Category.values()[Random().nextInt(Category.values().size)],
//                zzz,
//                "2 гантели 8п, Бицепс 15п, Попа 10п", 2 * 3,
//                i
//            )
//        )
//
//        id++
//    }

    return list
}