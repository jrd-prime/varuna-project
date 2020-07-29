package ru.jrd_prime.trainingdiary.handlers

import ru.jrd_prime.trainingdiary.fb_core.models.Premium
import ru.jrd_prime.trainingdiary.fb_core.models.User

interface UserPremium {
    fun onGetUserPremium(
        premium: Premium?,
        uid: String
    )

}

interface UserPremiumChange {


    fun onChangeUserPremium(
        premium: Premium?,
        uid: String
    )
}

interface UserInfo {
    fun onChangeUserInfo(user: User?)
}

interface RefreshCallback {
    fun refreshActivity()
}