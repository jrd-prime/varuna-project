package ru.jrd_prime.trainingdiary.fb_core.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String?,
    val name: String?,
    val mail: String?,
    val fbUid: String?,
    var auth: Boolean?,
    val premium: Boolean?,
    val start: Long?,
    val end: Long?,
    val forever: Boolean?,
    val set: Boolean?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null)
}