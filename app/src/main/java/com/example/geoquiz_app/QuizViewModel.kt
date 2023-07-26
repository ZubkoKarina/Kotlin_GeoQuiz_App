package com.example.geoquiz_app

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModelK"

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var correctAnswers = 0
    private var answeredQuestions = 0
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val isQuestionAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered
    val isQuizFinished: Boolean
        get() = answeredQuestions == questionBank.size
    val score: Int
        get() = if (answeredQuestions > 0) {
            100 * correctAnswers / answeredQuestions
        } else {
            0
        }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }

    fun checkAnswer(userAnswer: Boolean): Boolean {
        return if (!questionBank[currentIndex].isAnswered) {
            questionBank[currentIndex].isAnswered = true
            answeredQuestions++
            if (userAnswer == currentQuestionAnswer) {
                correctAnswers++
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}
