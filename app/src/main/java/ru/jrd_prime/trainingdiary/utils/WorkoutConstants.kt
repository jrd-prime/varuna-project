package ru.jrd_prime.trainingdiary.utils

import ru.jrd_prime.trainingdiary.R

const val DATABASE_NAME = "varuna_db"
const val DATABASE_NAME_NEW = "varuna_new_db"
const val JP_DATE_FORMAT = "yyyy-MM-dd"



val CATEGORIES = mutableMapOf<Int, String>(
    0 to "empty",
    1 to "cardio",
    2 to "power",
    3 to "stretch",
    4 to "rest"
)
val catIcons =
    mutableMapOf<Int, Int>(
        0 to R.drawable.jp_question,
        1 to R.drawable.jp_heartbeat,
        2 to R.drawable.jp_dumbbell,
        3 to R.drawable.jp_child,
        4 to R.drawable.jp_sleep
    )
val catColor =
    mutableMapOf<Int, Int>(
        0 to R.color.colorWhite,
        1 to R.drawable.card_bg_red,
        2 to R.drawable.card_bg_blue,
        3 to R.drawable.card_bg_pink,
        4 to R.drawable.card_bg_yellow
    )
val catColorBGNoCorners =
    mutableMapOf<Int, Int>(
        0 to R.color.colorWhite,
        1 to R.drawable.card_bg_red_no_corners,
        2 to R.drawable.card_bg_blue_no_corners,
        3 to R.drawable.card_bg_pink_no_corners,
        4 to R.drawable.card_bg_yellow_no_corners
    )
val lightGrey = R.color.jpLight

