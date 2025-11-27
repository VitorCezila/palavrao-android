package com.cezila.domain.use_case

import com.cezila.domain.error.SubmitGuessError
import com.cezila.domain.error.WordError
import com.cezila.domain.model.GameRules
import com.cezila.domain.model.GuessOutput
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.Word
import com.cezila.domain.repository.DictionaryRepository
import com.cezila.domain.Result
import com.cezila.domain.model.Language
import com.cezila.domain.rules.GuessEvaluator

class SubmitGuessUseCase(
    private val dictionaryRepository: DictionaryRepository,
    private val rules: GameRules = GameRules()
) {

    operator fun invoke(
        round: Round,
        rawGuess: String,
        language: Language
    ): Result<GuessOutput, SubmitGuessError> {
        if (round.status != RoundStatus.IN_PROGRESS) return Result.Error(SubmitGuessError.ROUND_FINISHED)

        val guess = when (val wRes = Word.of(rawGuess, rules.wordLength)) {
            is Result.Success -> wRes.data
            is Result.Error -> when (wRes.error) {
                WordError.INVALID_LENGTH -> return Result.Error(SubmitGuessError.INVALID_LENGTH)
                WordError.INVALID_CHARS -> return Result.Error(SubmitGuessError.INVALID_CHARS)
            }
        }
        if (!dictionaryRepository.isWordValid(guess, language)) {
            return Result.Error(SubmitGuessError.INVALID_WORD)
        }

        val result = GuessEvaluator.evaluate(guess, round.target)
        val attempts = round.attempts + guess

        val finished = result.isWin || attempts.size >= rules.maxAttempts
        val status = when {
            result.isWin -> RoundStatus.WON
            finished -> RoundStatus.LOST
            else -> RoundStatus.IN_PROGRESS
        }

        val newRound = round.copy(
            attempts = attempts,
            status = status,
            lastResult = result
        )
        return Result.Success(GuessOutput(newRound, result))
    }
}