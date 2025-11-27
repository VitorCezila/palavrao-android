package com.cezila.data.repository.avalanche

import android.content.res.AssetManager
import com.cezila.domain.Result
import com.cezila.domain.error.StartAvalancheRoundError
import com.cezila.domain.model.Language
import com.cezila.domain.model.Word
import com.cezila.domain.repository.SettingsRepository
import com.cezila.domain.repository.avalanche.AvalancheRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class LocalRandomAvalancheRepository(
    private val assets: AssetManager,
    private val settingsRepository: SettingsRepository,
    private val random: Random = Random.Default
) : AvalancheRepository {

    private val wordListsCache = mutableMapOf<Language, List<String>>()

    override suspend fun fetchWord(): Result<Word, StartAvalancheRoundError> {
        return try {
            val currentLanguage = settingsRepository.getLanguage().first()

            val wordList = getOrLoadWordList(currentLanguage)

            if (wordList.isEmpty()) {
                return Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
            }

            val pick = wordList[random.nextInt(wordList.size)]

            when (val r = Word.of(pick, 5)) {
                is Result.Success -> Result.Success(r.data)
                is Result.Error -> Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
            }
        } catch (_: Exception) {
            Result.Error(StartAvalancheRoundError.COMMUNICATION_ERROR)
        }
    }

    private fun getOrLoadWordList(language: Language): List<String> {
        if (wordListsCache.containsKey(language)) {
            return wordListsCache[language]!!
        }

        val list = try {
            assets.open(language.dictionaryPath).bufferedReader().useLines { lines ->
                lines
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { Word.normalize(it) }
                    .filter { it.length == 5 && it.all { ch -> ch in 'A'..'Z' } }
                    .toList()
            }
        } catch (_: Exception) {
            emptyList()
        }

        if (list.isNotEmpty()) {
            wordListsCache[language] = list
        }
        return list
    }
}