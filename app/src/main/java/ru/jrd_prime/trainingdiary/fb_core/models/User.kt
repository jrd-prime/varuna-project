package ru.jrd_prime.trainingdiary.fb_core.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String?,
    val name: String?,
    val mail: String?,
    val fbUid: String?,
    var auth: Boolean?
) {
    constructor() : this(id = null, name = null, mail = null, fbUid = null, auth = null)
}