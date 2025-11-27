package com.cezila.domain.model.daily

import com.cezila.domain.model.Word

data class DailyWordInfo(
    val word: Word,
    val referenceDate: String
)