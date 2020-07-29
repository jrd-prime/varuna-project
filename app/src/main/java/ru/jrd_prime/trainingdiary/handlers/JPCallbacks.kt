package ru.jrd_prime.trainingdiary.handlers

import ru.jrd_prime.trainingdiary.fb_core.models.User


interface UserInfo {
    fun onChangeUserInfo(user: User?)
}

interface RefreshCallback {
    fun refreshActivity()
}