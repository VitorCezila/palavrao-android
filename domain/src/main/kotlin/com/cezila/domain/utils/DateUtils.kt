package com.cezila.domain.utils

import java.time.ZonedDateTime

object DateUtils {

    fun toDayString(isoDate: String): String {
        return try {
            val zdt = ZonedDateTime.parse(isoDate)
            zdt.toLocalDate().toString()
        } catch (_: Exception) {
            isoDate.take(10)
        }
    }

    fun millisToDayString(millis: Long): String {
        val zdt = java.time.Instant.ofEpochMilli(millis)
            .atZone(java.time.ZoneId.systemDefault())
        return zdt.toLocalDate().toString()
    }

}