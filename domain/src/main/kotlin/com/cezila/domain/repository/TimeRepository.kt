package com.cezila.domain.repository

interface TimeRepository {

    suspend fun getCurrentTimeMillis(): Long?

}