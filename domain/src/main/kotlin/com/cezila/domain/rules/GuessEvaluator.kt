package com.cezila.domain.rules

import com.cezila.domain.model.GuessResult
import com.cezila.domain.model.LetterFeedback
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Word

object GuessEvaluator {
    /**
     * 1) Marca CORRECT (posição exata).
     * 2) Conta sobras do alvo.
     * 3) Marca PRESENT até o limite das sobras; resto ABSENT.
     */
    fun evaluate(guess: Word, target: Word): GuessResult {
        val g = guess.value
        val t = target.value
        require(g.length == t.length)

        val n = t.length
        val feedback = Array(n) { LetterFeedback(g[it], LetterState.ABSENT) }
        val usedTarget = BooleanArray(n)

        // CORRECT
        for (i in 0 until n) {
            if (g[i] == t[i]) {
                feedback[i] = LetterFeedback(g[i], LetterState.CORRECT)
                usedTarget[i] = true
            }
        }

        // Remaining count
        val remaining = mutableMapOf<Char, Int>()
        for (i in 0 until n) {
            if (!usedTarget[i]) {
                val c = t[i]
                remaining[c] = (remaining[c] ?: 0) + 1
            }
        }

        // Passo 3: PRESENT/ABSENT
        for (i in 0 until n) {
            if (feedback[i].state == LetterState.CORRECT) continue
            val c = g[i]
            val left = remaining[c] ?: 0
            if (left > 0) {
                feedback[i] = LetterFeedback(c, LetterState.PRESENT)
                remaining[c] = left - 1
            } else {
                feedback[i] = LetterFeedback(c, LetterState.ABSENT)
            }
        }

        val isWin = feedback.all { it.state == LetterState.CORRECT }
        return GuessResult(feedback.toList(), isWin)
    }
}