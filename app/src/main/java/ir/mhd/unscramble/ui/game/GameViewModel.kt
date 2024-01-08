package ir.mhd.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _currentWordCount = MutableStateFlow(0)
    val currentWordCount: StateFlow<Int> = _currentWordCount.asStateFlow()

    private val _currentScrambledWord = MutableStateFlow<String?>(null)
    val currentScrambledWord = _currentScrambledWord.asStateFlow()

    private val wordsList: MutableList<String> = mutableListOf()

    private lateinit var currentWord: String


    init {
        getNextWord()
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        if (wordsList.contains(currentWord)) {
            getNextWord()
        }

        val tempWord = currentWord.toCharArray()

        do {
            tempWord.shuffle()
        } while (String(tempWord).equals(currentWord, false))

        Log.d("Unscramble", "currentWord= $currentWord")
        _currentScrambledWord.value = String(tempWord)
        _currentWordCount.update {
            it + 1
        }
        wordsList.add(currentWord)
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
        _isGameOver.value = false
    }

    private fun increaseScore() {
        _score.update {
            it + SCORE_INCREASE
        }
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            _isGameOver.value = true
            false
        }
    }
}