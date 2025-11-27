package com.cezila.core_designsystem.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.cezila.core_designsystem.R
import com.cezila.domain.model.LetterState
import com.cezila.domain.model.Round
import com.cezila.domain.model.RoundStatus
import com.cezila.domain.rules.GuessEvaluator

class GameShareManager(private val context: Context) {

    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun shareAvalanche(word: String?, streak: Int) {
        val targetWord = word ?: ""
        val text = context.getString(R.string.share_avalanche_content, targetWord, streak)
        copyToClipboard(text)
    }

    fun shareDaily(referenceDate: String, round: Round?, streak: Int) {
        if (round == null) return

        val attempts = round.attempts.size
        val maxAttempts = round.rules.maxAttempts

        val headerBuilder = StringBuilder(
            context.getString(R.string.share_daily_header, referenceDate, attempts, maxAttempts)
        )

        if (round.status == RoundStatus.WON) {
            headerBuilder.append(context.getString(R.string.share_daily_streak_suffix, streak))
        }

        val grid = formatEmojiGrid(round)
        val finalText = "$headerBuilder\n\n$grid"
        copyToClipboard(finalText)
    }

    fun shareChallenge(challengeCode: String, round: Round?) {
        if (round == null) return

        val attempts = round.attempts.size
        val maxAttempts = round.rules.maxAttempts

        val headerBuilder = StringBuilder(
            context.getString(R.string.share_challenge_header, challengeCode, attempts, maxAttempts)
        )

        if (round.status == RoundStatus.WON) {
            headerBuilder.append(" 🔥")
        }

        val grid = formatEmojiGrid(round)
        val finalText = "$headerBuilder\n\n$grid"
        copyToClipboard(finalText)
    }

    fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText("share_text", text)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(
            context,
            context.getString(R.string.share_toast_copied),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun formatEmojiGrid(round: Round): String {
        return round.attempts.joinToString("\n") { attempt ->
            val result = GuessEvaluator.evaluate(attempt, round.target)

            result.feedback.joinToString("") { feedback ->
                when (feedback.state) {
                    LetterState.CORRECT -> "🟩"
                    LetterState.PRESENT -> "🟨"
                    LetterState.ABSENT -> "⬛"
                }
            }
        }
    }
}