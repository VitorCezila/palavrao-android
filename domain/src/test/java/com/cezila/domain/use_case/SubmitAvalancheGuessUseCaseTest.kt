package com.cezila.domain.use_case

import com.cezila.domain.error.SubmitAvalancheGuessError
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.model.Word
import com.cezila.domain.repository.FakeDictionaryRepository
import com.cezila.domain.result.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.test.assertTrue

class SubmitAvalancheGuessUseCaseTest {

    private fun useCase(
        validWords: Set<String> = setOf("SALTO", "LASSO", "LASER", "LENTE", "LIMAO", "LIVRO", "NATAL", "SALAO")
    ) =
        SubmitAvalancheGuessUseCase(
            dictionaryRepository = FakeDictionaryRepository(validWords)
        )

    private fun w(s: String): Word =
        when (val r = Word.of(s)) {
            is Result.Error   -> error("word factory failed in test: $s -> ${r.error}")
            is Result.Success -> r.data
        }


    @Test
    fun `first try correct WON with 1 attempt`() {
        val uc = useCase()
        val round0 = Round(target = w("SALTO"))

        val res = uc(round0, "SALTO")
        assertTrue(res is Result.Success)
        val out = (res as Result.Success).data
        assertTrue(out.result.isWin)
        assertEquals(RoundStatus.WON, out.round.status)
        assertEquals(1, out.round.attempts.size)
        assertEquals("SALTO", out.round.attempts.first().value)
    }

    @Test
    fun `six wrong attempts LOST and cannot continue`() {
        val uc = useCase()
        val target = w("SALTO")
        var round = Round(target = target)

        val guesses = listOf("LASSO", "LASER", "LENTE", "LIMAO", "LIVRO", "NATAL")
        guesses.forEachIndexed { i, g ->
            val res = uc(round, g)
            assertTrue(res is Result.Success, "attempt ${i + 1} should be success path")
            round = res.data.round
            if (i < 5) assertEquals(RoundStatus.IN_PROGRESS, round.status)
        }

        assertEquals(6, round.attempts.size)
        assertEquals(RoundStatus.LOST, round.status)

        val res7 = uc(round, "LASSO")
        assertTrue(res7 is Result.Error)
        assertEquals(SubmitAvalancheGuessError.ROUND_FINISHED, (res7 as Result.Error).error)
    }

    @Test
    fun `invalid word throws error`() {
        val uc = useCase(validWords = setOf("SALTO", "LASSO"))
        val round = Round(target = w("SALTO"))

        val res = uc(round, "XXXXX")
        assertTrue(res is Result.Error)
        assertEquals(SubmitAvalancheGuessError.INVALID_WORD, (res as Result.Error).error)
    }


    @Test
    fun `accented input is normalized and accepted`() {
        val uc = useCase(validWords = setOf("SALAO"))
        val round = Round(target = w("SALAO"))

        val res = uc(round, "salão")
        assertTrue(res is Result.Success)
        val out = (res as Result.Success).data
        assertTrue(out.result.isWin)
        assertEquals(RoundStatus.WON, out.round.status)
        assertEquals("SALAO", out.round.attempts.first().value)
    }

}