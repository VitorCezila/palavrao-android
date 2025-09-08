package com.cezila.data.repository

import android.content.res.AssetManager
import com.cezila.domain.Result
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.model.Word
import com.cezila.domain.repository.AvalancheRepository
import kotlin.random.Random

class LocalRandomAvalancheRepository(
    assets: AssetManager,
    path: String = "dict/ptbr_5.txt",
    private val random: Random = Random.Default
) : AvalancheRepository {

    private val wordList: List<String> = runCatching {
        assets.open(path).bufferedReader().useLines { lines ->
            lines.asSequence()
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .map { Word.normalize(it) }
                .filter { it.length == 5 && it.all { ch -> ch in 'A'..'Z' } }
                .toList()
        }
    }.getOrElse { emptyList() }

    override suspend fun fetchWord(): Result<Word, StartAvalancheRoundError> {
        if (wordList.isEmpty()) return Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
        val pick = wordList[random.nextInt(wordList.size)]
        return when (val r = Word.of(pick, 5)) {
            is Result.Success -> Result.Success(r.data)
            is Result.Error   -> Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
        }
    }
}