package ru.jrd_prime.trainingdiary.utils.cfg

class AppConfig {
    companion object {
        const val SHARED_PREFERENCE_NAME = "settings.jp"
        const val SHARED_PREFERENCE_NAME_FOR_CARD = "opened_cards.jp"
        const val SP_NAME_FIRST_RUN = "appFirstRun"
        const val SP_NAME_USER_AUTH = "isUserAuthenticated"
        const val SP_NAME_USER_NAME = "userName"
        const val SP_NAME_USER_MAIL = "userMail"
        const val SP_NAME_USER_ID = "userID"
        const val SP_NAME_USER_REMOTE_PHOTO_URI = "userRemotePhotoURI"
        const val SP_NAME_USER_PHOTO_ON_DEVICE = "userPhotoOnDevice"
        const val GLOBAL_TAG = "--- TAG ---"

    }

    fun getPrefName(): String? {
        return SHARED_PREFERENCE_NAME
    }

    fun getPrefNameFirstRun(): String? {
        return SP_NAME_FIRST_RUN
    }

    fun getPrefIsUserAuth(): String? {
        return SP_NAME_USER_AUTH
    }

    fun getPrefUserName(): String? {
        return SP_NAME_USER_NAME
    }

    fun getSpNameUserID(): String? {
        return SP_NAME_USER_ID
    }

    fun getSpNameUserRemotePhotoURI(): String? {
        return SP_NAME_USER_REMOTE_PHOTO_URI
    }

    fun getSpNameUserPhotoOnDevice(): String? {
        return SP_NAME_USER_PHOTO_ON_DEVICE
    }

    fun getPrefUserMail(): String? {
        return SP_NAME_USER_MAIL
    }

    fun getGlobalTag(): String? {
        return GLOBAL_TAG
    }

}