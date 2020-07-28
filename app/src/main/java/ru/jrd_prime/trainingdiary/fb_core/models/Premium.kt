package ru.jrd_prime.trainingdiary.fb_core.models

import org.threeten.bp.LocalDateTime
import ru.jrd_prime.trainingdiary.utils.dateToTimestamp

data class Premium(
    val premium: Boolean = false,
    val start: Long = dateToTimestamp(LocalDateTime.now()),
    val end: Long = dateToTimestamp(LocalDateTime.now()),
    val userID: String = "",
    val forever: Boolean = false,
    val set: Boolean = false
) {}

