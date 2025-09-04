package com.cezila.domain.rules

import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Word
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import com.cezila.domain.Result

class GuessEvaluatorTest {

    private fun w(s: String): Word =
        when (val r = Word.of(s)) {
            is Result.Success -> r.data
            is Result.Error   -> throw AssertionError("Word.of('$s') failed with ${r.error}")
        }

    @Test
    fun `all correct returns win with all CORRECT`() {
        val target = w("BANCO")
        val guess  = w("BANCO")

        val res = GuessEvaluator.evaluate(guess, target)

        assertTrue(res.isWin)
        res.feedback.forEach { f -> assertEquals(LetterState.CORRECT, f.state) }
    }

    @Test
    fun `all absent gives all ABSENT`() {
        val target = w("FLOTA")
        val guess  = w("CERUM")
        val res = GuessEvaluator.evaluate(guess, target)

        assertTrue(res.feedback.all { it.state == LetterState.ABSENT })
        assertEquals(false, res.isWin)
    }

    @Test
    fun `present letters respect remaining counts (classic present case)`() {
        // target: S A L T O
        // guess : L A S S O
        // exp   : [PRESENT (L), CORRECT (A), PRESENT (S), ABSENT (S), CORRECT (O)]
        val target = w("SALTO")
        val guess  = w("LASSO")

        val res = GuessEvaluator.evaluate(guess, target)
        val states = res.feedback.map { it.state }

        assertEquals(
            listOf(
                LetterState.PRESENT,
                LetterState.CORRECT,
                LetterState.PRESENT,
                LetterState.ABSENT,
                LetterState.CORRECT
            ),
            states
        )
        assertEquals(false, res.isWin)
    }

    @Test
    fun `repeated letter in guess exceeds count becomes ABSENT`() {
        // target: C O B R A
        // guess : C A C A O
        // exp   : [CORRECT (C), PRESENT (A), ABSENT (C), ABSENT (A), PRESENT (O)]
        val target = w("COBRA")
        val guess  = w("CACAO")

        val res = GuessEvaluator.evaluate(guess, target)
        val states = res.feedback.map { it.state }

        assertEquals(
            listOf(
                LetterState.CORRECT,
                LetterState.PRESENT,
                LetterState.ABSENT,
                LetterState.ABSENT,
                LetterState.PRESENT
            ),
            states
        )
        assertEquals(false, res.isWin)
    }

    @Test
    fun `mixed case with duplicates where some are CORRECT and others ABSENT`() {
        // target: C A S A L
        // guess : C A C A U
        // exp   : [CORRECT (C), CORRECT (A), ABSENT (C), CORRECT (A), ABSENT (U)]
        val target = w("CASAL")
        val guess  = w("CACAU")

        val res = GuessEvaluator.evaluate(guess, target)
        val states = res.feedback.map { it.state }

        assertEquals(
            listOf(
                LetterState.CORRECT,
                LetterState.CORRECT,
                LetterState.ABSENT,
                LetterState.CORRECT,
                LetterState.ABSENT
            ),
            states
        )
        assertEquals(false, res.isWin)
    }

}